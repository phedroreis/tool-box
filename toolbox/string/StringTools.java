package toolbox.string;

import java.util.Arrays;

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
        
        char[] ch = new char[length];
        
        Arrays.fill(ch, c);
        
        return new String(ch);       
        
    }//repeatChar 
    
    /***************************************************************************
    * Substitui, em um objeto <b><i>StringBuilder</i></b>, todas as ocorrencias
    * de uma determinada string por outra.
    *
    * @param sb O objeto <b><i>StringBuilder</i></b>.
    *
    * @param target A string a ser substituida.
    * 
    * @param replacement A string de substituicao.
    ***************************************************************************/    
    public static void replace(
        final StringBuilder sb,
        final String target, 
        final String replacement
    ) {   
        
        int p; int length = target.length();
        
        while ((p = sb.indexOf(target)) != -1) 
            sb.replace(p, p + length, replacement); 
            
    }//replace
    
}//classe StringTools
