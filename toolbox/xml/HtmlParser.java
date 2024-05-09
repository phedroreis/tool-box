package toolbox.xml;
  
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import javax.management.modelmbean.XMLParseException;

/**
 * Escreva uma descrição da classe ParseXml aqui.
 * 
 * @author Pedro Reis 
 * @version 1.0 - 23 de abril de 2024
 */
public final class HtmlParser {
    
    private final toolbox.regex.Regex tagRegex;
    
    private final String htmlContent;
    
    private final TagParser tagParser;
    
    private LinkedList<Tag> stack;
    
    private static String msg$1, msg$2;
    
    static {
        try {
            ResourceBundle rb = 
                ResourceBundle.getBundle("toolbox.properties.HtmlParser", toolbox.locale.Localization.getLocale());
            msg$1 = rb.getString("msg$1");//has no corresponding open tag       
            msg$2 = rb.getString("msg$2");//do not close

            
       } catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "has no corresponding open tag";        
            msg$2 = "do not close";
          
       }
    }    
    
    /**
     * 
     */
    public HtmlParser(final String htmlContent, final TagParser tagParser) {
        
        this.htmlContent = htmlContent;
        
        tagRegex = new toolbox.regex.Regex("</?(\\w+)(.*?)>");
        tagRegex.setTarget(htmlContent);
        
        stack = new LinkedList<>();
        
        this.tagParser = tagParser;
        
    }//construtor
    
    /*
     * 
     */
    private void popStack(final int endContentIndex, final int endBlockIndex) {
        
        Tag tag = stack.pop();
        
        if (tag.isNotifyClosingRequired()) {
            
            tag.setContent(htmlContent.substring(tag.getStartContentIndex(), endContentIndex));
            tag.setEndBlockIndex(endBlockIndex);
            tagParser.closeTag(tag);  
            
        }        
    }
    
    /*
     * 
     */
    private void exception(final String tagMatchedName) throws XMLParseException {
        
        Tag topTag = stack.peek();
        
        String topTagName = (topTag == null) ? msg$1 : (msg$2 + " <" + topTag.getTagName() + '>');
        
        throw new XMLParseException("</" + tagMatchedName + "> " + topTagName);
        
    }//exception
    
    /*
     * 
     */
    private String getTopTagName() {
        
        Tag tag = stack.peek();
        return (tag == null) ? "#" : tag.getTagName();
        
    }
    
    /**
     * 
     */
    public void parse() throws XMLParseException {
        
        String tagMatched;
        
        Tag topTag;
        
        while ((tagMatched = tagRegex.find()) != null) { 
            
            String tagMatchedName = tagRegex.group(1).toLowerCase();
            
            int tagMatchedPosition = tagRegex.start();
           
            String topTagName = getTopTagName();  
            
            if (tagMatched.charAt(1) == '/') {

                if (!tagMatchedName.equals(topTagName)) {
                
                    if (topTagName.equals("li") || topTagName.equals("p")) {
                        
                         popStack(tagMatchedPosition, tagMatchedPosition + tagMatched.length());
                         
                         topTagName = getTopTagName();
                         
                         if (topTagName == "#") exception(tagMatchedName); 
                         
                    }
                    
                    if (tagMatchedName.equals("html") && topTagName.equals("body")) {
                        
                         popStack(tagMatchedPosition, tagMatchedPosition + tagMatched.length());
                         
                         topTagName = getTopTagName();
                         
                         if (!topTagName.equals("html")) exception(tagMatchedName);
                    }
                    
                    if (!tagMatchedName.equals(topTagName)) exception(tagMatchedName);  
                    
                }//if  
                 
                popStack(tagMatchedPosition, tagMatchedPosition + tagMatched.length());
              
            } else {
               
                Tag tag = new Tag(tagMatchedName, tagRegex.group(2), tagMatchedPosition, tagMatchedPosition + tagMatched.length());
                
                switch (tagMatchedName) {
                    //self-closing tags
                    case "area":
                    case "base":
                    case "br":
                    case "col":
                    case "embed":
                    case "hr":    
                    case "img":
                    case "input":
                    case "link":    
                    case "meta":
                    case "param":    
                    case "source":
                    case "track":
                    case "wbr":
                    case "command"://obsoleta
                    case "keygen"://obsoleta
                    case "menuitem"://obsoleta
                    case "frame"://obsoleta    
                        break;
                    
                    //<body> fecha <head> se estiver aberta                  
                    case "body":
                        if (topTagName.equals("head")) popStack(tagMatchedPosition, tagMatchedPosition + tagMatched.length());//fecha com <                        
                        stack.push(tag);
                        break;
                    
                    //<li> e <p> fecham <li> e <p> (respecitvamente) se estiverem abertas    
                    case "li":
                    case "p":    
                        if (topTagName.equals(tagMatchedName)) popStack(tagMatchedPosition, tagMatchedPosition + tagMatched.length());
                        stack.push(tag);
                        break;
                    
                    //restantes requerem tag de fechamento                   
                    default: 
                        stack.push(tag);
                        
                }//switch
                
                tagParser.openTag(tag);
                
            }//if-else
            
        }//while
        
        while ( (topTag = stack.peek()) != null) {
            
            popStack(htmlContent.length(), htmlContent.length());
            
        }
        
    }//parse
    
    /**
     * 
     */
    @Override
    public String toString() {
        
        return htmlContent;
        
    }//toString    

}//classe HtmlParser
