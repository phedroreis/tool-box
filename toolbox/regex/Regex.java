package toolbox.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/*******************************************************************************
* Uma classe para localizar expressões regulares em strings.
*
* @since 1.0 - 14 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
* @see java.util.regex.Pattern
*******************************************************************************/
public final class Regex {
    
    private final Pattern pattern;
    
    private Matcher matcher;    
   
    /***************************************************************************
    * Construtor.
    *
    * @param regex Uma expressão regular sintaticamente válida.
    *
    * @throws PatternSyntaxException Caso haja erro de sintaxe no argumento
    * <b><i>regex</i></b>.
    ***************************************************************************/
    public Regex(final String regex) throws PatternSyntaxException {
        
        matcher = null;
        
        pattern = Pattern.compile(regex);  
        
    }//construtor
    
    /***************************************************************************
    * Define a String na qual a pesquisa será realizada.
    *
    * @param target A String a ser pesquisada.
    * 
    * @throws NullPointerException Se <b><i>target</i></b> for <code>null</code>.
    ***************************************************************************/
    public void setTarget(final String target) throws NullPointerException {        
   
        matcher = pattern.matcher(target);  
        
    }//setTarget
    
    /***************************************************************************
    * Retorna a string que correspondeu ao padrao ou <code>null</code> se nao
    * for mais encontrada ocorrencia do padrao.
    *
    * <p>Na primeira vez que o metodo for chamado, ira localizar a primeira
    * substring que corresponda ao padrao (se houver, caso contrario retorna
    * <code>null</code>).</p>
    *
    * <p>E cada chamada subsequene, o método retorna a próxima substring
    * a corresponder com o padrão, se houver. Se não houver substring que
    * corresponda, o método retornará <code>null</code>.</p>
    *
    * @return A próxima substring localizada, ou <code>null</code> se não houver
    * mais substrings para localizar.
    *
    ***************************************************************************/
    public String find() {
        
        if (matcher.find()) return matcher.group(); else return null;    
        
    }//find()
    
    /***************************************************************************
    * Obtém um grupo na <code>String</code> localizada pelo padrão.
    *
    * @param group O índice do grupo.
    *
    * @return A substring que correspondeu ao grupo, ou <code>null</code> se não
    * foi econtrada correspondência para este grupo, se nenhum "match" foi
    * tentado ainda, se o indice do grupo nao corresponde a um grupo existente
    * na regex ou se o match tentado previamente retornou <code>false</code>.
    * 
    ***************************************************************************/
    public String group(final int group) {
        
        String s;
        
        try {
            
            s = matcher.group(group);
        }
        catch (IndexOutOfBoundsException | IllegalStateException e) {
            
            return null;
        }
 
        return s;
        
    }//group
    
    /**
     * Retorna o indice do match previo.
     * 
     * @return O indice do ultimo match.
     * 
     * @throws IllegalStateException Se nenhum match foi tentado ainda ou se a ultima execucao do 
     * metodo {@link find() find()} retornou <code>false</code>. 
     */
    public int start() throws IllegalStateException {
        
        return matcher.start();
        
    }//start
    
}//classe Regex
