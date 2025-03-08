import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
class Ates{
    private int x;
    private int y;

    public Ates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

public class Oyun extends JPanel implements KeyListener, ActionListener {

    Timer timer = new Timer(5,this); // hareket için
    // 2 değer bekler 1. si : timer ın kaç milisaniyede çalışacağı ne kadar küçük bir değer olursa
    // o kadar hızlı hareket eder top
    // 2. si : ActionListener interface ini implemente eden bir obje

    private int gecenSure = 0;
    private int harcananAtes= 0;
    private BufferedImage image; // burada uzay gemisi dosyasını okumam gerekiyor

    private ArrayList<Ates> atesler = new ArrayList<Ates>();

    private int atesdirY = 1;
    private int topX = 0;
    private int topdirX = 2; // sağa sola kayabilmesi için
    private int uzayGemisiX = 0;
    private int diruzayX = 20; // sağa sola kayabilmesi için


    public boolean kontrolEt(){

        for( Ates ates : atesler){
            if(new Rectangle(ates.getX(),ates.getY(),10,20).intersects(new Rectangle(topX,0,20,20))){
                return true;
            }

        }
        return false;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        gecenSure += 5;


        g.setColor(Color.red); // topun rengi

        // top her hareket ettiğinde topX gğncellenmiş olacak
        // her renk çağrılışında top hareket edecek
        g.fillOval(topX,0,20,20); // topu çiziyorum ve böylece daire şeklinde bir top oluştu

        g.drawImage(image,uzayGemisiX,490,image.getWidth() / 10,image.getHeight() / 10,this); // uzay gemisini çiziyorum

        for(Ates ates : atesler){
            if(ates.getY() < 0 ){
                atesler.remove(ates);
            }
        }
        g.setColor(Color.BLUE);

        for(Ates ates : atesler){
            g.fillRect(ates.getX(), ates.getY(),10,20);

        }
        if(kontrolEt()){
            timer.stop();
            String message = "Kazandınız...\n"+
                             "Harcanan ateş : " + harcananAtes+
                             "\nGeçen süre : " + gecenSure / 1000.0 + " saniye";
            JOptionPane.showMessageDialog(this,message);
            System.exit(0);

        }


    }

    @Override
    public void repaint() { // paint işleminin yapılması için bunu çağırmam gerekiyor
        super.repaint();
    }

    public Oyun() {

        try {
            image = ImageIO.read(new FileImageInputStream(new File("uzaygemisi.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setBackground(Color.BLACK);

        // timer başlayınca oyunun direkt başlamasını istiyorum
        timer.start();
        // timer başladığında her 5 milisaniyede bir action metodu çalışacak


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // hareket için bu metodun çalışması gerekir

        for(Ates ates : atesler){
            // ateşlerin yukarı gitmesini sağlıyorum
            ates.setY(ates.getY() - atesdirY);

        }


        topX += topdirX;
        // oyun başladığında top sağa doğru gidecek fakat ben sona geldiğinde geri dönmesini istiyorum
        if(topX >= 750){
            topdirX = -topdirX;
        }
        // ve topum geriye giderken -li değerlere sahip olmaması lazım
        if(topX <= 0){
            topdirX = -topdirX;

        }
        repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {  // sol tuş ile sola sağ tuş ile sağa hareket sağlar

        int c = e.getKeyCode();

        if(c == KeyEvent.VK_LEFT){
            if( uzayGemisiX <= 0){

                uzayGemisiX = 0;

            }
            else {
                uzayGemisiX -= diruzayX;
            }


        } else if (c == KeyEvent.VK_RIGHT) {
            if(uzayGemisiX >= 750){
                uzayGemisiX = 750;

            }
            else {
                uzayGemisiX += diruzayX;

            }


        } else if (c == KeyEvent.VK_CONTROL) {
            atesler.add(new Ates(uzayGemisiX+15,470));

            harcananAtes++;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
