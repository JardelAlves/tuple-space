import net.jini.core.entry.Entry;

import java.util.ArrayList;

public class VMs implements Entry {
    private static final long serialVersionUID = 1L;
    public String tipo;
    public String nome;
    public String host;
    public ArrayList<String> processos;
    public VMs() { }
}
