import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    public String nome;
    public String vmSelecionada;
    public JavaSpace space;
    public List<Processos> processosList = new ArrayList<Processos>();
    public List<VMs> vmsList = new ArrayList<VMs>();
    public List<Hosts> hostsList = new ArrayList<Hosts>();
    public List<Nuvens> nuvensList = new ArrayList<Nuvens>();

    public Cliente() { }

    public void ConectarEspaco() {
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            Lookup finder = new Lookup(JavaSpace.class);
            this.space = (JavaSpace) finder.getService();
            if (this.space == null) {
                System.out.println("O servico JavaSpace não foi encontrado. Encerrando...");
                System.exit(-1);
            }
            System.out.println("Conectado com o espaço de Tupla");
            System.out.println(this.space);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean CriarProcesso(String nome, String vm) throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
        Processos processo = new Processos();
        processo.tipo = "processo";
        processo.nome = nome;
        processo.vm = vm;
        
        Processos resposta = (Processos) this.space.read(processo, null, 3 * 1000);

        if(resposta == null) {
            processosList.add(processo);
            this.space.write(processo, null, 3600 * 1000);

            return true;
        }
        else {
            if(!processo.nome.contains(resposta.nome)){
                processosList.add(processo);
                this.space.write(processo, null, 3600 * 1000);

                this.nome = nome;
                this.vmSelecionada = vm;

                return true;
            }
            else {
                System.out.println("Não foi possível criar o processo, pois já existe outro com o mesmo nome na mesma VM.");

                return false;
            }
        }
    }

    public Boolean CriarVM(String nome, String host) throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
        VMs vm = new VMs();
        vm.tipo = "vm";
        vm.nome = nome;
        vm.host = host;
        
        VMs resposta = (VMs) this.space.read(vm, null, 3 * 1000);

        if(resposta == null) {
            vmsList.add(vm);
            this.space.write(vm, null, 3600 * 1000);

            return true;
        }
        else {
            if(!vm.nome.contains(resposta.nome)){
                vmsList.add(vm);
                this.space.write(vm, null, 3600 * 1000);

                return true;
            }
            else {
                System.out.println("Não foi possível criar a VM, pois já existe outra com o mesmo nome no mesmo host.");

                return false;
            }
        }
    }

    public Boolean CriarHost(String nome, String nuvem) throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
        Hosts host = new Hosts();
        host.tipo = "host";
        host.nome = nome;
        host.nuvem = nuvem;
        
        Hosts resposta = (Hosts) this.space.read(host, null, 3 * 1000);

        if(resposta == null) {
            hostsList.add(host);
            this.space.write(host, null, 3600 * 1000);

            return true;
        }
        else {
            if(!host.nome.contains(resposta.nome)){
                hostsList.add(host);
                this.space.write(host, null, 3600 * 1000);

                return true;
            }
            else {
                System.out.println("Não foi possível criar o host, pois já existe outro com o mesmo nome na mesma nuvem.");

                return false;
            }
        }
    }

    public Boolean CriarNuvem(String nome) throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
        Nuvens nuvem = new Nuvens();
        nuvem.tipo = "nuvem";
        nuvem.nome = nome;
        
        Nuvens resposta = (Nuvens) this.space.read(nuvem, null, 3 * 1000);

        if(resposta == null) {
            nuvensList.add(nuvem);
            this.space.write(nuvem, null, 3600 * 1000);

            return true;
        }
        else {
            if(!nuvem.nome.contains(resposta.nome)){
                nuvensList.add(nuvem);
                this.space.write(nuvem, null, 3600 * 1000);

                return true;
            }
            else {
                System.out.println("Não foi possível criar a nuvem, pois já existe outra com o mesmo nome.");

                return false;
            }
        }
    }

    public void EnviarMensagem(String destinatario, String texto) throws RemoteException, TransactionException {
        Mensagem mensagem = new Mensagem();

        mensagem.destinatario = destinatario;
        mensagem.vm = this.vmSelecionada;
        mensagem.nome = this.nome;
        mensagem.conteudo = texto;

        this.space.write(mensagem, null, 300 * 1000);
    }

    public void ReceberMensagem() throws TransactionException, UnusableEntryException, RemoteException, InterruptedException {
        Mensagem template = new Mensagem();

        template.destinatario = this.nome;
        template.vm = this.vmSelecionada;

        Mensagem mensagem = (Mensagem) this.space.take(template, null, (long) (2 * 1000));

        if (mensagem != null) {
            System.out.println("\r\n" + mensagem.nome + ": " + mensagem.conteudo + "\r\n");
        }
    }
}