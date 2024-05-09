package toolbox.xml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.management.modelmbean.XMLParseException;

/**
 * Escreva uma descrição da classe ParseXml aqui.
 * 
 * @author Pedro Reis 
 * @version 1.0 - 23 de abril de 2024
 */
public final class XmlParser {
    
    private final toolbox.regex.Regex tagRegex;
    
    private final String xmlContent;
    
    private final TagParser tagParser;
    
    private LinkedList<Tag> stack;
    
    /**
     * 
     */
    public XmlParser(final String xmlContent, final TagParser tagParser) {
        
        this.xmlContent = xmlContent;
        
        tagRegex = new toolbox.regex.Regex("</?(\\w+)(.*?)>");
        tagRegex.setTarget(xmlContent);
        
        stack = new LinkedList<>();
        
        this.tagParser = tagParser;
        
    }//construtor
    
    /**
     * 
     */
    public void parse() throws Exception {
        
        String tagMatched;
        Tag tag;
        
        while ((tagMatched = tagRegex.find()) != null) { 
            
            String tagMatchedName = tagRegex.group(1).toLowerCase();
            
            int tagMatchedPosition = tagRegex.start();
            
            if (tagMatched.charAt(1) == '/') {
                
                try {
                    
                    tag = stack.pop();
                }
                catch (NoSuchElementException e) {
                    
                    throw new XMLParseException("_ -> </" + tagMatchedName + '>');                    
                }
                
                if (!tagMatchedName.equals(tag.getTagName())) 
                    throw new XMLParseException('<' + tag.getTagName() + "> -> </" + tagMatchedName + '>');

                if (tag.isNotifyClosingRequired()) {
                    
                    tag.setContent(xmlContent.substring(tag.getStartContentIndex(), tagMatchedPosition));
                    tag.setEndBlockIndex(tagMatchedPosition + tagMatched.length());
                    tagParser.closeTag(tag);
                  
                }                      
               
            } else {
                
                  tag = new Tag(tagMatchedName, tagRegex.group(2), tagMatchedPosition, tagMatchedPosition + tagMatched.length());
                
                  if (!tagMatched.endsWith("/>")) stack.push(tag);
                
                  tagParser.openTag(tag);
                
            }//if-else
            
        }//while
        
    }//parse
    
    /**
     * 
     */
    @Override
    public String toString() {
        
        return xmlContent;
        
    }//toString
    
}//classe XmlParser
