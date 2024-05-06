package toolbox.xml;


/**
 * Escreva uma descrição da classe TagParser aqui.
 * 
 * @author Pedro Reis 
 * @version 1.0 - 24 de abril de 2024
 */
public abstract class TagParser {
    
    abstract public void openTag(final Tag tag);
    
    abstract public void closeTag(final Tag tag);   

}//classe TagParser
