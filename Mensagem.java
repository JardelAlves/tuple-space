import net.jini.core.entry.Entry;

public class Mensagem implements Entry {
    private static final long serialVersionUID = 1L;
    public String destinatario;
    public String vm;
    public String nome;
    public String conteudo;
    public Mensagem() {
    }
}
