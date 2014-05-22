/*
 * This code was created by Jeff Molofee '99 
 * (merged to Linux/SDL by evik form Sean Farrel's 
 * Linux/SDL code.
 * 
 *
 * If you've found this code useful, please let me know.
 *
 * Visit Jeff at http://nehe.gamedev.net/
 * 
 * or for port-specific comments, questions, bugreports etc. 
 * email to evik@chaos.hu
 */

#include <stdio.h>
#include <stdlib.h>
#include <GL/gl.h>
#include <GL/glu.h>
#include "SDL.h"

/* screen width, height, and bit depth */
#define SCREEN_WIDTH  1024
#define SCREEN_HEIGHT  768
#define SCREEN_BPP      32

/* Set up some booleans */
#define TRUE  1
#define FALSE 0

/* This is our SDL surface */
SDL_Surface *surface;

/* User Defined Variables */

GLboolean	sp;								/* Spacebar Pressed? */


GLfloat	xrot = 0;		/* Use For Rotation Of Objects */
GLfloat yrot = 0;
GLfloat zrot = 0;


void Quit( int returnCode )
{
	  /* freeing up texture data memory */
    free(tex_data);
	
		/* clean up the window */
    SDL_Quit( );

    /* and exit appropriately */
    exit( returnCode );
}

/* The function called when our window is resized (which shouldn't happen, because we're fullscreen) */
void resizeWindow(int Width, int Height)
{
  if (Height==0)				/* Prevent A Divide By Zero If The Window Is Too Small */
    Height=1;

  glViewport(0, 0, Width, Height);		/* Reset The Current Viewport And Perspective Transformation */
}

void Reset (void)														/* Reset The Maze, Colors, Start Point, Etc */
{
}



/* The main drawing function. */
void drawGLScene()
{
}

/* A general OpenGL initialization function.  Sets all of the initial parameters. */
void initGL()	        /* We call this right after our OpenGL window is created. */
{
}

/* function to handle key press events */
void handleKeyPress( SDL_keysym *keysym )
{
    switch ( keysym->sym )
	{
	case SDLK_ESCAPE:
	    /* ESC key was pressed */
	    Quit( 0 );
	    break;
	case SDLK_F1:
	    /* F1 key was pressed
	     * this toggles fullscreen mode
	     */
	    SDL_WM_ToggleFullScreen( surface );
	    break;
	case SDLK_SPACE:
			/* Spacebar pressed */
			Reset();
			break;
	default:
	    break;
	}

    return;
}


int main(int argc, char **argv) 
{
 /* Flags to pass to SDL_SetVideoMode */
    int videoFlags;
    /* main loop variable */
    int running = TRUE;
    /* used to collect events */
    SDL_Event event;
    /* this holds some info about our display */
    const SDL_VideoInfo *videoInfo;
    /* whether or not the window is active */
    int isActive = TRUE;
		/* TickCount */
		GLuint lastTickCount;
		GLuint tickCount;

    /* initialize SDL */
    if ( SDL_Init( SDL_INIT_VIDEO ) < 0 )
		{
	    fprintf( stderr, "Video initialization failed: %s\n",
		     SDL_GetError( ) );
	    Quit( 1 );
		}

		lastTickCount = SDL_GetTicks();
		
    /* Fetch the video info */
    videoInfo = SDL_GetVideoInfo( );

    if ( !videoInfo )
		{
	    fprintf( stderr, "Video query failed: %s\n",
		     SDL_GetError( ) );
	    Quit( 1 );
		}

    /* the flags to pass to SDL_SetVideoMode */
    videoFlags  = SDL_OPENGL;          /* Enable OpenGL in SDL */
    videoFlags |= SDL_GL_DOUBLEBUFFER; /* Enable double buffering */
    videoFlags |= SDL_HWPALETTE;       /* Store the palette in hardware */
    videoFlags |= SDL_RESIZABLE;       /* Enable window resizing */

    /* This checks to see if surfaces can be stored in memory */
    if ( videoInfo->hw_available )
			videoFlags |= SDL_HWSURFACE;
    else
			videoFlags |= SDL_SWSURFACE;

    /* This checks if hardware blits can be done */
    if ( videoInfo->blit_hw )
			videoFlags |= SDL_HWACCEL;

    /* Sets up OpenGL double buffering */
    SDL_GL_SetAttribute( SDL_GL_DOUBLEBUFFER, 1 );

    /* get a SDL surface */
    surface = SDL_SetVideoMode( SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_BPP,
				videoFlags );

    /* Verify there is a surface */
    if ( !surface )
		{
	    fprintf( stderr,  "Video mode set failed: %s\n", SDL_GetError( ) );
	    Quit( 1 );
		}

		/*SDL_WM_ToggleFullScreen( surface );*/

		SDL_WM_SetCaption("Lesson 42: Multiple Viewports... 2003 NeHe Productions... Building Maze!", NULL);

    /* initialize OpenGL */
    initGL( );

    /* resize the initial window */
    resizeWindow( SCREEN_WIDTH, SCREEN_HEIGHT );
  
    /* wait for events */
    while ( running )
	  {
	    /* handle the events in the queue */

	    while ( SDL_PollEvent( &event ) )
			{
		    switch( event.type )
				{
					case SDL_ACTIVEEVENT:
						/* Something's happend with our focus
						 * If we lost focus or we are iconified, we
						 * shouldn't draw the screen
						 */
						if ( event.active.gain == 0 )
							isActive = FALSE;
						else
							isActive = TRUE;
			    break;			    
					case SDL_VIDEORESIZE:
					  /* handle resize event */
					  surface = SDL_SetVideoMode( event.resize.w,
								event.resize.h,	SCREEN_BPP, videoFlags );
			    if ( !surface )
					{
				    fprintf( stderr, "Could not get a surface after resize: %s\n", SDL_GetError( ) );
				    Quit( 1 );
					}
			    resizeWindow( event.resize.w, event.resize.h );
			    break;
				case SDL_KEYDOWN:
			    /* handle key presses */
			    handleKeyPress( &event.key.keysym );
			    break;
				case SDL_QUIT:
			    /* handle quit requests */
			    running = FALSE;
			    break;
				default:
			    break;
				}
			}
    
			tickCount = SDL_GetTicks();
			Update(tickCount - lastTickCount);
			lastTickCount = tickCount; 
    	/* draw the scene */
	    if ( isActive )
				drawGLScene( );
			
		}
  
    /* clean ourselves up and exit */
    Quit( 0 );

    /* Should never get here */
    return( 0 );
}

