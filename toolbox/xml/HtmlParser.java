package toolbox.xml;
  
import java.util.HashMap;
import java.util.LinkedList;
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
    private void popStack(final int endContentIndex) {
        
        Tag tag = stack.pop();
        
        if (tag.isNotifyClosingRequired()) {
            
            tag.setContent(htmlContent.substring(tag.getStartContentIndex(), endContentIndex));
            tagParser.closeTag(tag);  
            
        }        
    }
    
    /*
     * 
     */
    private void exception(final String tagMatchedName) throws XMLParseException {
        
        Tag topTag = stack.peek();
        
        String topTagName = (topTag == null) ? "#" : topTag.getTagName();
        
        throw new XMLParseException("unmatched tags: " + topTagName + " -> " + tagMatchedName);
        
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
            
            topTag = stack.peek();
            
            String topTagName = (topTag == null) ? "#" : topTag.getTagName();  
            
            if (tagMatched.charAt(1) == '/') {

                if (!tagMatchedName.equals(topTagName)) {
                
                    if (topTagName.equals("li") || topTagName.equals("p")) {
                        
                         popStack(tagMatchedPosition);
                         
                         topTag = stack.peek();
                         
                         if (topTag == null) exception(tagMatchedName); 
                         
                         if (!tagMatchedName.equals(topTag.getTagName())) exception(tagMatchedName); 
                         
                    }
                    else exception(tagMatchedName); 
                    
                }//if  
                 
                popStack(tagMatchedPosition);
              
            } else {
               
                Tag tag = new Tag(tagMatchedName, tagRegex.group(2), tagMatchedPosition + tagMatched.length());
                
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
                        if (topTagName.equals("head")) popStack(tagMatchedPosition);//fecha com <                        
                        stack.push(tag);
                        break;
                    
                    //<li> e <p> fecham <li> e <p> (respecitvamente) se estiverem abertas    
                    case "li":
                    case "p":    
                        if (topTagName.equals(tagMatchedName)) popStack(tagMatchedPosition);
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
            
            popStack(htmlContent.length());
            
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
