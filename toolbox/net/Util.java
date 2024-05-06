package toolbox.net;
import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


/**
 * Escreva uma descrição da classe Util aqui.
 * 
 * @author (seu nome) 
 * @version (um número da versão ou uma data)
 */
public final class Util {
    
    /**
     * 
     */
    public static void downloadUrlToPathname(
         final String url,
         final String pathname
    ) throws IOException, MalformedURLException {
            
        URL theUrl = new URL(url);
        
        try (

            FileOutputStream fos = new FileOutputStream(pathname);

            ReadableByteChannel rbc = Channels.newChannel(theUrl.openStream());

        ) {

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }    
        
    }//downloadUrlToPathname()

}
