package Parte1;

import com.example.pecl_zombis.HelloController;
import com.example.pecl_zombis.ListaZonasH;
import com.example.pecl_zombis.ListaZonasZ;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;


public class Mundo extends javax.swing.JFrame {

    private javax.swing.JLabel TituloRef;
    private javax.swing.JLabel TituloDes;
    private javax.swing.JLabel Comida;
    private javax.swing.JLabel TituloCom;
    private javax.swing.JLabel TitulComun;
    private javax.swing.JLabel TituloTun;
    private javax.swing.JLabel TituloExt;
    private ArrayList<JTextArea> zonasEntradaTunelText= new ArrayList<>();
    private ArrayList<JTextArea> zonasSalidaTunelText= new ArrayList<>();
    private ArrayList<JTextArea> zonasTunelText= new ArrayList<>();
    private JTextArea DescansoText;
    private JTextArea ComedorText;
    private JTextArea zonaComunText;
    private JTextArea ComidaText;

    private ArrayList<JTextArea> zonasExtTextH= new ArrayList<>();
    private ArrayList<JTextArea> zonasExtTextZ= new ArrayList<>();
    protected ListaZonasH Descanso ;
    protected ListaZonasH comedor ;
    protected ListaZonasH zonaComun ;
    protected List<ListaZonasH> zonasEntradaTunel = new ArrayList<>();
    protected List<ListaZonasH> zonasSalidaTunel = new ArrayList<>();
    protected List<ListaZonasH> zonasTunel = new ArrayList<>();
    protected List<ListaZonasH> zonasInseguras = new ArrayList<>();
    private List<ListaZonasZ> zonasInsegurasZ= new ArrayList<>();
    ArrayList<Semaphore> tuneles = new ArrayList<>();
    private ReentrantLock[] lockTuneles= new ReentrantLock[4];
    private Condition[] condEntradaTunel= new Condition[4];
    private Condition[] condSalidaTunel= new Condition[4];

    protected int[] esperandoEntrada= new int[4];
    protected int[] esperandoSalida= new int[4];

    public ArrayList<Humano> humanos= new ArrayList<>();
    public ArrayList<Zombie> zombis= new ArrayList<>();
    ArrayList<CyclicBarrier>  barrerasTuneles= new ArrayList<>();
    private Comida comida;
    static volatile boolean pausado=false;

    public Mundo() throws InterruptedException{
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        initComponents();
        Descanso = new ListaZonasH(DescansoText);
        comedor = new ListaZonasH(ComedorText);
        zonaComun = new ListaZonasH(zonaComunText);
        Comida comida = new Comida(ComidaText);

        this.comida = comida;

        for (int i=0 ;i<4 ;i++){
            zonasInseguras.add(new ListaZonasH(zonasExtTextH.get(i)));
            zonasInsegurasZ.add(new ListaZonasZ(zonasExtTextZ.get(i)));
            zonasEntradaTunel.add(new ListaZonasH(zonasEntradaTunelText.get(i)));
            zonasSalidaTunel.add(new ListaZonasH(zonasSalidaTunelText.get(i)));
            zonasTunel.add(new ListaZonasH(zonasTunelText.get(i)));
            tuneles.add(new Semaphore(1,true));
            barrerasTuneles.add(new CyclicBarrier(3));
            lockTuneles[i] = new ReentrantLock();
            condEntradaTunel[i] = lockTuneles[i].newCondition();
            condSalidaTunel[i] = lockTuneles[i].newCondition();
            esperandoEntrada[i] = 0;
            esperandoSalida[i] = 0;
        }
        this.setVisible(true);

    }
    public ListaZonasZ getZonasInsegurasZ(int zona) {
        return zonasInsegurasZ.get(zona);
    }
    public Comida getComida() {
        return comida;
    }

    public List<Humano> getContador_humanos(int zona) {
        {
            return zonasInseguras.get(zona).getLista();
        }
    }


    private void initComponents() {

        Font fontGrande = new Font("SansSerif", Font.BOLD, 16);

        zonaComunText = crearTextArea(fontGrande,200,70);
        ComedorText = crearTextArea(fontGrande,200,70);
        DescansoText = crearTextArea(fontGrande,200,70);
        ComidaText = crearTextArea(fontGrande,300,30);

        zonasEntradaTunelText = new ArrayList<>();
        zonasTunelText = new ArrayList<>();
        zonasSalidaTunelText = new ArrayList<>();
        zonasExtTextH = new ArrayList<>();
        zonasExtTextZ = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            zonasEntradaTunelText.add(crearTextArea(fontGrande,200,70));
            zonasTunelText.add(crearTextArea(fontGrande,200,30));
            zonasSalidaTunelText.add(crearTextArea(fontGrande,200,70));
            zonasExtTextH.add(crearTextArea(fontGrande,200,70));
            zonasExtTextZ.add(crearTextArea(fontGrande,200,70));
        }

        // Panel Refugio
        JPanel panelRefugio = new JPanel(new GridLayout(5, 2, 10, 10));
        panelRefugio.setBorder(BorderFactory.createTitledBorder("Refugio"));
        panelRefugio.add(new JLabel("Zona Común"));
        panelRefugio.add(wrapScroll(zonaComunText,200,70));
        panelRefugio.add(new JLabel("Comedor"));
        panelRefugio.add(wrapScroll(ComedorText,200,70));
        panelRefugio.add(new JLabel("Comida"));
        panelRefugio.add(wrapScroll(ComidaText,30,30));
        panelRefugio.add(new JLabel("Descanso"));
        panelRefugio.add(wrapScroll(DescansoText,200,70));

        // Panel Túneles
        JPanel panelTuneles = new JPanel(new GridLayout(5, 3, 10, 10));
        panelTuneles.setBorder(BorderFactory.createTitledBorder("Túneles"));
        panelTuneles.add(new JLabel("Entrada"));
        panelTuneles.add(new JLabel("Túnel"));
        panelTuneles.add(new JLabel("Salida"));

        for (int i = 0; i < 4; i++) {
            panelTuneles.add(wrapScroll(zonasEntradaTunelText.get(i),200,30));
            panelTuneles.add(wrapScroll(zonasTunelText.get(i),200,30));
            panelTuneles.add(wrapScroll(zonasSalidaTunelText.get(i),200,30));
        }

        // Panel Zona Exterior
        JPanel panelExterior = new JPanel(new GridLayout(5, 2, 10, 10));
        panelExterior.setBorder(BorderFactory.createTitledBorder("Zona Exterior"));
        panelExterior.add(new JLabel("Humanos"));
        panelExterior.add(new JLabel("Zombis"));

        for (int i = 0; i < 4; i++) {
            panelExterior.add(wrapScroll(zonasExtTextH.get(i),200,70));
            panelExterior.add(wrapScroll(zonasExtTextZ.get(i),200,70));
        }

        // Contenedor principal
        JPanel contenedor = new JPanel(new GridLayout(1, 3, 20, 20));
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contenedor.add(panelRefugio);
        contenedor.add(panelTuneles);
        contenedor.add(panelExterior);

        // Ventana
        setTitle("Simulación Apocalipsis Zombi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setContentPane(contenedor);

    }

    private JTextArea crearTextArea(Font font,int alto, int ancho) {
        JTextArea area = new JTextArea();
        area.setFont(font);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setPreferredSize(new Dimension(alto,ancho));
        area.setWrapStyleWord(true);
        return area;
    }

    private JScrollPane wrapScroll(JTextArea area,int alto, int ancho) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(alto, ancho));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }
    public static boolean isPausado(){
        return pausado;
    }
    public void setPausado(boolean pausado) {
        Mundo.pausado = pausado;
    }
    public boolean atacar_Humano(Zombie zombie,Humano presa,int zona)  {
        presa.setPeleando();
        presa.setAtacado();
        try {
            sleep((int) (Math.random() * 1000)+500);
            int ataqueExitoso=(int)(Math.random()*100);
            if (ataqueExitoso<66){
                Logs.getInstancia().logInfo("El zombi "+zombie.getZombieId()+ " no ha podido matar al humano "+presa.getHumanoId());
                System.out.println("El zombi "+zombie.getZombieId()+ " no ha podido matar al humano "+presa.getHumanoId());
                presa.setLibre();
                return false;
            } else{
                System.out.println("El zombi " +zombie.getZombieId()+" ha logrado matar al humano  " +presa.getHumanoId());
                Logs.getInstancia().logInfo("El humano "+presa.getHumanoId()+" ha muerto");

                String id= String.valueOf(presa.getHumanoId());
                String nuevoid= id.replace("H", "Z");
                Zombie nuevoZombi= new Zombie(nuevoid,this);
                zonasInseguras.get(zona).sacar(presa);
                humanos.remove(presa);
                presa.setMuerto();
                zombis.add(nuevoZombi);
                nuevoZombi.start();
                return true;

            }
        } catch(Exception e){
            System.out.println("Error sucedido al atacar al humano " + presa.getId());
            System.out.println(e);
            return false;
        }

    }
    public int getNumeroHumanosTuneles(int zona) {
        if (zona >= 0 && zona < zonasInseguras.size()) {
            return zonasEntradaTunel.get(zona).getLista().size()+
                    zonasSalidaTunel.get(zona).getLista().size()+
                    zonasTunel.get(zona).getLista().size();
        } else{
            return -1;
        }
    }
    public int getNumeroHumanosRefugio() {
        int totalHumanos = 0;
        totalHumanos += zonaComun.getLista().size();
        totalHumanos += comedor.getLista().size();
        totalHumanos += Descanso.getLista().size();
        return totalHumanos;
    }



    public List<Zombie> getRankingZombis() {
        List<Zombie> ranking = new ArrayList<>();

        for (ListaZonasZ zona : zonasInsegurasZ) {
            ranking.addAll(zona.getLista());
        }
        List<Zombie> top3 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Zombie topZombie = null;
            for (Zombie z : ranking) {
                if(!top3.contains(z)&&(topZombie==null||z.getContadorMuertes()>topZombie.getContadorMuertes())){
                    topZombie=z;
                }
            }
            if (topZombie!= null) {
                top3.add(topZombie);
            }

        }
        return top3;
    }
}
