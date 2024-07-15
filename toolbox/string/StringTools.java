package toolbox.string;

import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/*******************************************************************************
* Métodos estáticos para manipulação de strings.
*
* @since 1.0 - 14 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
*******************************************************************************/
public final class StringTools {
    
    public static final String NEWLINE;
    
    private static String msg$1;
    private static String msg$2;
    
    static {
        
       NEWLINE = System.lineSeparator();
        
        try {
            ResourceBundle rb = 
                ResourceBundle.getBundle("toolbox.properties.StringTools", toolbox.locale.Localization.getLocale());
            msg$1 = rb.getString("msg$1");//      
            msg$2 = rb.getString("msg$2");//
            
       } catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "Negative length";        
            msg$2 = "String null";
          
       }        
    
    }
    
    /***************************************************************************
    * Retorna uma string contendo o caractere <b><i>c</i></b> repetido 
    * <b><i>length</i></b> vezes. Se <b><i>length</i></b> = 0 retorna string
    * vazia.
    *
    * @param c O caractere.
    *
    * @param length O comprimento da string.
    *
    * @return Um objeto <b><i>String</i></b> contendo o caractere 
    * <b><i>c</i></b> repetido <b><i>length</i></b> vezes.
    * 
    * @throws IllegalArgumentException Se <b><i>length</i></b> < 0.
    ***************************************************************************/
    public static String repeatChar(final char c, final int length) 
        throws IllegalArgumentException {    
            
        if (length < 0) throw new IllegalArgumentException(msg$1);    
        
        char[] ch = new char[length];
        
        Arrays.fill(ch, c);
        
        return new String(ch);       
        
    }//repeatChar 
    
    /***************************************************************************
    * Retorna uma string contendo a string <b><i>s</i></b> repetida 
    * <b><i>length</i></b> vezes.
    *
    * @param s A String
    *
    * @param length O numero de repetiçoes.
    *
    * @return Um objeto <b><i>String</i></b> contendo a string
    * <b><i>s</i></b> repetida <b><i>length</i></b> vezes.
    * 
    * @throws IllegalArgumentException Se <b><i>length</i></b> < 0.
    * 
    * @throws NullPointerException Se <b><i>s</i></b> for <code>null</code>.
    ***************************************************************************/
    public static String repeatString(final String s, final int length) 
        throws IllegalArgumentException, NullPointerException {   
        
        if (length < 0) throw new IllegalArgumentException(msg$1);
        
        if (s == null) throw new NullPointerException(msg$2);
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) sb.append(s);
        
        return sb.toString();       
        
    }//repeatString  
    
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
