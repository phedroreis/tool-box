package toolbox.string;

/*******************************************************************************
* Métodos estáticos para manipulação de strings.
*
* @since 1.0 - 14 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
*******************************************************************************/
public final class StringTools {
    
    /***************************************************************************
    * Retorna uma string contendo o caractere <b><i>c</i></b> repetido 
    * <b><i>length</i></b> vezes.
    *
    * @param c O caractere.
    *
    * @param length O comprimento da string.
    *
    * @return Um objeto <b><i>String</i></b> contendo o caractere 
    * <b><i>c</i></b> repetido <b><i>length</i></b> vezes.
    ***************************************************************************/
    public static String repeatChar(final char c, final int length) {         
        
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) s.append(c);
        return s.toString();
        
    }//repeatChar    
    
}//classe StringTools
