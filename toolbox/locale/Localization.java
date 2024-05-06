package toolbox.locale;

import java.util.Locale;


/**
 * Classe para configuraçao de internacionalizacao.
 * 
 * <p>Os padroes linguisticos-culturais exibidos pelo codigo do programa serao estabelecidos pelo objeto Locale passado ao metodo 
 * {@link Localization#setLocale(java.util.Locale) setLocale} desta classe. Cada classe do programa deve fornecer um arquivo
 * properties que correponda a este Locale (ou seja, com pares chave/valor apontando para as strings no idioma definido pelo Locale).</p>
 * 
 * <p>Este metodo deve ser chamando uma unica vez em todo o programa e sua chamada deve estar em um bloco de inicializacao static na
 * classe que contiver o metodo main() de inicializaçao. Cuidando para que seja a primeira instruçao a ser executada pelo programa, antes
 * mesmo de qualquer atribuiçao de variavel.</p>
 * 
 *<p>Tambem, um bloco static de inicializacao como o exemplificado abaixo, deve existir em cada classe que precise traduzir strings 
 *literais para o idioma definido pelo Locale. Se este bloco nao existir, sera usado o Locale default (en_US).</p>
 *
 *<pre>
 *<code>
  public class InputParserYesOrNo extends InputParser{
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;
    private static String msg$4;
    
    static {
        try {
            ResourceBundle rb = 
                ResourceBundle.getBundle("toolbox.properties.InputParserYesOrNo", toolbox.locale.Localization.getLocale());
            msg$1 = rb.getString("msg$1");//y        
            msg$2 = rb.getString("msg$2");//yes
            msg$3 = rb.getString("msg$3");//n
            msg$4 = rb.getString("msg$4");//no
            
       } catch (NullPointerException | MissingResourceException | ClassCastException e) {
           
            // Opcaoes default caso falhe a chamada a rb.getString() [Locale en_US : default]
            msg$1 = "y"        
            msg$2 = "yes"
            msg$3 = "n";
            msg$4 = "no";
          
       }
    }
 *</code>
 *</pre>
 *
 *<p>Note que o parametro <b><i>toolbox.properties.InputParserYesOrNo</i></b> deve corresponder ao arquivo properties 
 *referente ao locale retornado pelo metodo getLocale() desta classe. Que retorna o Locale default (en_US) ou o Locale
 *setado pelo metodo setLocale().No exemplo acima a classe pertence ao pacote toolbox.terminal e o arquivo properties
 *buscado esta inserido no pacote toolbox.properties</p>
 *
 *<p>E recomendavel colocar todos os arquivos properties no mesmo pacote do projeto e nomea-los com o mesmo nome da classe que
 *os utiliza.</p>
 *
 * 
 * @author Pedro Reis 
 * @since 1.0 - 23 de março de 2024
 * @version 1.0
 */
public final class Localization {
    
    private static Locale globalLocale = new Locale("en", "US");
    /**
     * Define o Locale global que deve ser usado em todo o programa. 
     * 
     * <p>So deve ser executado uma unica vez e na inicializacao da classe que
     * contem o metodo main(). Em um bloco static na inicializacao desta classe,
     * sendo o primeiro codigo a ser executado pelo programa.</p>
     * 
     * <p>Se nao for executado, chamadas subsequentes ao metodo getLocale() desta 
     * classe obterao o Locale default (en_US).</p>
     * 
     * @param locale O Locale que deve ser o global para todo o programa.
     */
    public static void setLocale(final Locale locale) {
        globalLocale = locale;
    }//setLocale
    
    /**
     * Retorna o Locale global.
     * 
     * @return O Locale global.
     */
    public static Locale getLocale() {
        return globalLocale;
    }//getLocale


}//classe Localization
