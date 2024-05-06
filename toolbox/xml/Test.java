package toolbox.xml;
import java.util.*;


/**
 * Escreva uma descrição da classe Test aqui.
 * 
 * @author (seu nome) 
 * @version (um número da versão ou uma data)
 */
public class Test {
    
        
    public static void main(String[] a) throws Exception, java.io.IOException {
        
        System.out.println("iniciando...\n");
        
        toolbox.textfile.TextFileHandler tfh = new toolbox.textfile.TextFileHandler("/home/userhugo/Downloads/clip.html");
        
        tfh.read();
        
        String htmlContent = tfh.getContent();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
        
        XmlParser xmlParser = new XmlParser(htmlContent, new ThisParser());
        
        //xmlParser.parse();
        
        System.out.println("\n\nAgora como arquivo HTML...\n\n");                                                                                                                                                                                                                                          
        
        HtmlParser HtmlParser = new HtmlParser(htmlContent, new ThisParser());
        
        HtmlParser.parse();        
        
    }
    
    private static class ThisParser extends TagParser {
        
              
        @Override
        public void openTag(final Tag tag) {
            
            String tagName = tag.getTagName();
            
            tag.notifyClosing();
            
            System.out.println("abriu tag:" + tagName);
            
            System.out.print("\nATRIBUTOS de " + tagName + " :");
            
            HashMap<String, String> map = tag.getAttrMap();
            
            for (String key: map.keySet()) System.out.print(" (" + key + " = " + map.get(key) + ")");  
            
            System.out.println("\n");
            
        }
        
        @Override
        public void closeTag(final Tag tag) {
            
            System.out.println("CONTEUDO de " + tag.getTagName() + ":\n");
            
            System.out.println(tag.getContent());
            
            System.out.println("\nfechou tag:" + tag.getTagName() + "\n");
                  
        }
    }

}
