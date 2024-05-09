package toolbox.xml;

import java.util.HashMap;


/**
 * Um objeto dessa classe armazena e fornece os dados de tags XML e HTML.
 * 
 * <p>O nome da tag, a lista de atributos (se houver) e possivelmente o 
 * conteudo do escopo da tag.</p>
 * 
 * @author Pedro Reis
 * @version 1.0 - 24 de abril de 2024 
 */
public final class Tag {
    
    private final String tagName;
    
    private final HashMap<String, String> attrMap;
    
    private String content;
    
    private final int startBlockIndex;
    
    private int endBlockIndex;
    
    private final int startContentIndex;
    
    private boolean notifyClosing;
    
    /**
     * Apenas objetos das classes XMLParser e HTMLParser devem criar objetos Tag.
     * 
     * @param tagName O nome da tag.
     * 
     * @param tagAttrs A <code>String</code> com a lista de atributos da tag.
     * 
     * @param startContentIndex A posicao no arquivo do primeiro caractere do escopo da tag.
     * Sem efeito para tags self-closing.
     */
    protected Tag(final String tagName, final String tagAttrs, final int startBlockIndex, final int startContentIndex) {
        
        this.tagName = tagName;        

        attrMap = getAttrMap(tagAttrs);
        
        this.startBlockIndex = startBlockIndex;
        
        this.startContentIndex = startContentIndex;
        
        notifyClosing = false;
        
        content = null;
        
        endBlockIndex = -1;
        
    }
     
    /*
     * Retorna um mapa com os pares chave/valor de todos os atributos da tag.
     */
    private HashMap<String, String> getAttrMap(final String tag) {
        
        toolbox.regex.Regex regex = new toolbox.regex.Regex(" (.+?)=\"(.+?)\"");
        regex.setTarget(tag);
        
        HashMap<String, String> mapKeyValue = new HashMap<>();
        
        String attr;
        
        while ((attr = regex.find()) != null) {
            
            mapKeyValue.put(regex.group(1), regex.group(2));
            
        }
        
        return mapKeyValue;
        
    }//getAttrMap
    
    /**
     * Retorna o indice do primeiro caractere do escopo da tag.
     * 
     * @return Indice do primeiro caractere do escopo da tag.
     */
    public int getStartContentIndex() {
        
        return startContentIndex;
        
    }//getStartContentIndex
     
    /**
     * 
     */
    public int getCloseTagIndex() {
        
        if (content == null) return -1;
        
        return startContentIndex + content.length();
    }
    
    /**
     * 
     */
    protected void setEndBlockIndex(final int endBlockIndex) {
        
        this.endBlockIndex = endBlockIndex;
        
    }
    
    /**
     * 
     */
    public int getStartBlockIndex() {
        
        return startBlockIndex;
    }    
    
    /**
     * 
     */
    public int getEndBlockIndex() {
        
        return endBlockIndex;
        
    }    
    
    /**
     * Retorna o mapa com os pares chaves/valor dos atributos da tag.
     * 
     * @return Um <code>HashMap</code> com os pares chaves/valor com os atributos da tag.
     */
    public HashMap<String, String> getAttrMap() {
        
        return attrMap;
        
    }//getAttrMap
    
    /**
     * Retorna o nome da tag.
     * 
     * @return O nome da tag.
     */
    public String getTagName() {
        
        return tagName;
        
    }//getTagName
    
    /**
     * Apenas objetos das classes XMLParser e HTMLParser devem chamar este metodo.
     * 
     * @param content O conteudo do escopo da tag.
     */
    protected void setContent(final String content) {
        
        this.content = content;
        
    }//setContent
    
    /**
     * Retorna o conteudo da tag, que eh atribuido por um objeto da classe XMLParser ou HTMLParser. 
     * Ou <code>null</code> caso ainda nao tenha sido atribuido.
     * 
     * @return O conteudo da tag, que eh atribuido por um objeto da classe XMLParser ou HTMLParser. 
     * Ou <code>null</code> caso ainda nao tenha sido atribuido.
     */
    public String getContent() {
        
        return content;
        
    }//getContent
    
    /**
     * Deve ser chamado por um objeto <code>TagParser</code> quando este for notificado da abertura 
     * da tag (com a chamada do seu metodo openTag), se for desejado que tambem o fechamento da tag
     * seja notificado. Nao tem efeito em caso de tags self-closing.
     */
    public void notifyClosing() {
        
        notifyClosing = true;
        
    }//notifyClosing
    
    /**
     * Para uso de objetos das classes <code>XMLParser e HTMLParser</code> apenas.
     */
    protected boolean isNotifyClosingRequired() {
        
        return notifyClosing;
        
    }//isNotifyClosingRequired

}//classe Tag
