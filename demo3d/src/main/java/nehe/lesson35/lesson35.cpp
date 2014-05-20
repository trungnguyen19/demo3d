/**************************************
*                                     *
*     Playing AVI Files In OpenGL     *
*   using the avifile lib (>= 0.60)   *
*                                     *
*         Linux version by            *
*          Matthias Haack             *
*              2003                   *
*      matthias.haack@epost.de        *
*                                     *
*         Based on code of:           *
*      Jeff Molofee's Lesson 35       *
*       http://nehe.gamedev.net       *
*              2001                   *
*                                     *
**************************************/

// OpenGL specific
#include <GL/gl.h>
#include <GL/glu.h>
#include <GL/glut.h>

// Video specific

#include <avifile/avifile.h>
#include <avifile/version.h>

#if AVIFILE_MINOR_VERSION > 6
  #include <avifile/avm_default.h>
#else
  #include <avifile/default.h>
#endif

#include <avifile/image.h>
#include <avifile/StreamInfo.h>


#include <fstream>
#include <iostream>
#include <sys/types.h>

using namespace std;


// variables
int win_width        = 512;
int win_height       = 512;
int TEX_WIDTH        = 256;
int TEX_HEIGHT       = 256;
GLfloat tex_param_s  = 1.0f;
GLfloat tex_param_t  = 1.0f;
GLfloat aspect_ratio = 1.0f;
GLuint texture;                 // we need one texture later


int frame            = 0;

int msec;
int width;                      // width and height of video stream
int height;
double stream_fps = 0;          // fps (for playback, since we don't use avi player)

GLUquadricObj *quadratic;       // Storage For Our Quadratic Objects
avm::IReadFile *avifile = 0;      // represents the avi file
avm::IReadStream *avistream = 0;  // represents the avi stream
StreamInfo *streaminfo = 0;       // represents the avi streaminfo
CImage *image = 0;                // an image (provided by avifile)
uint8_t *ui_image_copy = 0;       // image data (for use with gluScaleImage and glTexImage2D)



// forward declarations
void display_func(void);
void keyboard_func(unsigned char, int, int);
void timer_func(int);
void initialize(void);


class FileNotOpen {
  char* info;
  public:
    FileNotOpen(char* info) { this->info = info; }
    void print() { cerr << info << endl << flush; }
};



// the main function
int main(int argc, char* argv[])
{



}




// let's set some things
void initialize(void)
{
}



// OpenGL/GLUT functions
void timer_func(int which_timer) {

  // if we got signal for timer 0, read one frame and create a texture of it
  if (which_timer == 0) {

    image = avistream->GetFrame(true);
    if (!avistream->Eof() && image ) {
      //gluScaleImage( GL_RGB, width, height, GL_UNSIGNED_BYTE, image->Data(0),
      //               TEX_WIDTH, TEX_HEIGHT, GL_UNSIGNED_BYTE, ui_image_copy );

      // this is about 30-40 times faster then using gluScaleImage(...)
      // for (int j=0; j<height; ++j)
      //   memcpy(&ui_image_copy[j*TEX_WIDTH*3], image->At(0,j), width*sizeof(uint8_t)*3);

    // avifile < v0.7 swaps top and bottom, lets fix this
#if AVIFILE_MINOR_VERSION < 7  
      for (int j=0; j<height; ++j)
        memcpy(&ui_image_copy[j*TEX_WIDTH*3], image->At(0, j), width*sizeof(uint8_t)*3);
#else
      for (int j=0; j<height; ++j)
        memcpy(&ui_image_copy[j*TEX_WIDTH*3], image->At(0, height-j-1), width*sizeof(uint8_t)*3);
#endif

      //glBindTexture( GL_TEXTURE_2D, texture );  // is done in initilaize() !
      // look at the GL_BGR (flips BGR video to RGB texture)
      glTexImage2D ( GL_TEXTURE_2D, 0, GL_RGB, TEX_WIDTH, TEX_HEIGHT, 0,
                     GL_BGR, GL_UNSIGNED_BYTE, ui_image_copy);

      image->Release();
    } else {
      avistream->Seek(0);  // back to start to stream
    }

    glutTimerFunc( (int) (1000.0f / stream_fps), timer_func, 0);  // re-set timer function
    glutPostRedisplay();   // redraw our scene
  }

}



void keyboard_func(unsigned char key, int x, int y) 
{

  // process keyboard input (no Fx keys!)
  switch(key) {

  case '0':
  case '1':
  case '2':
    effect = key - '0';
    cout << "effect " << effect << endl;
    display_func();
    break;

  case 'b':
    if (bg) {
      bg = false;
      cout << "background off" << endl;
    } else {
      bg = true;
      cout << "backgroud on" << endl;
    }
    display_func();
    break;

  case 'e':
    if (env) {
      env = false;
      cout << "environment mapping off" << endl;
    } else {
      env = true;
      cout << "environment mapping on" << endl;
    }
    display_func();
    break;


  case 'q':
  case 27:
    avistream->StopStreaming();
    exit(0);
    break; 
      
  default:
    break;  
    
  }
    
}



void display_func(void) 
{
 
}















