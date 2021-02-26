import net.jini.core.entry.Entry;

import java.util.ArrayList;

public class Hosts implements Entry {
    private static final long serialVersionUID = 1L;
    public String tipo;
    public String nome;
    public String nuvem;
    public ArrayList<String> vms;
    public Hosts() { }
}
