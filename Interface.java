import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

import java.rmi.RemoteException;

import java.util.Scanner;

public class Interface implements Entry {
    private static final long serialVersionUID = 1L;

    public Interface(Cliente cliente) {
        cliente.ConectarEspaco();
        
        try {
            cliente.CriarProcesso("jardel", "1");
            cliente.CriarHost("ja", "nuvem");
        } catch (TransactionException transactionException) {
            transactionException.printStackTrace();
        } catch (UnusableEntryException unusableEntryException) {
            unusableEntryException.printStackTrace();
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        ReceberMensagem(cliente);
        EnviarMensagem(cliente);
    }

    public void ReceberMensagem(Cliente cli){
        new Thread(() -> {
            System.out.println("Rodando a thread de recepção de mensagens");
            while (true){
                try {
                    cli.ReceberMensagem();
                } catch (TransactionException e) {
                    e.printStackTrace();
                } catch (UnusableEntryException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void EnviarMensagem(Cliente cli) {
        new Thread(() -> {
            System.out.println("Rodando a thread de envio de mensagens");
            Scanner scanner = new Scanner(System.in);
            while (true){
                    try {System.out.print("Digite o destinatário da mensagem (ENTER para sair): ");
                    String destinatario = scanner.nextLine();
                    if (destinatario == null || destinatario.equals("")) {
                        System.exit(0);
                    }
                    System.out.print("Digite o texto da mensagem (ENTER para sair): ");
                    String mensagem = scanner.nextLine();
                    if (mensagem == null || mensagem.equals("")) {
                        System.exit(0);
                    }
                    cli.EnviarMensagem(destinatario, mensagem);
                } catch (TransactionException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}