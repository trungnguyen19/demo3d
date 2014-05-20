package designpatterns.purejava;

public class Summary {
	public static void main(String[] args) {
		CreationalPatterns.AbstractFactory p = new Summary().new CreationalPatterns().new AbstractFactory();
	}

	class CreationalPatterns {
		class AbstractFactory {
			AbstractFactory() {
				// recognizeable by creational methods returning the factory
				// itself which in turn can be used to create another
				// abstract/interface type
				javax.xml.parsers.DocumentBuilderFactory.newInstance();
				javax.xml.transform.TransformerFactory.newInstance();
				javax.xml.xpath.XPathFactory.newInstance();
			}
		}

		class Builder {
			Builder() {
				// recognizeable by creational methods returning the instance
				// itself
				java.lang.StringBuilder stringBuilder;// .append();//
														// unsynchronized
				java.lang.StringBuffer stringBuffer;// .append();//synchronized
				java.nio.ByteBuffer byteBuffer;// .put() (also on CharBuffer,
												// ShortBuffer, IntBuffer,
												// LongBuffer, FloatBuffer and
												// DoubleBuffer)
				javax.swing.GroupLayout.Group group;// #addComponent()
				// All implementations of java.lang.Appendable

			}
		}

		class Factory {
			Factory() {
				java.util.Calendar.getInstance();
				// java.util.ResourceBundle.getBundle();
				java.text.NumberFormat.getInstance();
				// java.nio.charset.Charset.forName();
				// java.net.URLStreamHandlerFactory.createURLStreamHandler(String)
				// (Returns singleton object per protocol)
			}
		}
	}
}
