import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.FontMetrics;

public class TitleGame extends JPanel implements ActionListener {

    private static final int LARGURA = 800;
    private static final int ALTURA = 500;
    private static final int TAMANHO_BLOCO = 50;
    private static final int UNIDADES = LARGURA * ALTURA / (TAMANHO_BLOCO * TAMANHO_BLOCO);
    // O intervalo em mm vai impactar na velocidade 
    private static final int INTERVALO = 200;
    private static final String FONTE = "Roboto";
    private final int[] eixoX = new int[UNIDADES];
    private final int[] eixoy = new int[UNIDADES];
    private int corpoCobra = 2;
    private int blocosComidos;
    private int blocosX;
    private int blocosY;
    private char direcao = 'D'; // D - Direita, E - Esquerda, C - Cima, B - Baixo
    private boolean estaRodando = false;
    Timer timer;
    Random random;

    TitleGame() {
        random = new Random();
        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setBackground(new Color(233, 233, 233));
        setFocusable(true);
        addKeyListener(new LeitorDeTeclas());
        play();
    }

    private void play() {
        criarBloco();
        estaRodando = true;
        timer = new Timer(INTERVALO, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenharTela(g);
    }

    private void desenharTela(Graphics g) {
        if (estaRodando) {
            // Cor da comida 
            g.setColor(Color.red);
            g.fillOval(blocosX, blocosY, TAMANHO_BLOCO, TAMANHO_BLOCO);

            for (int i = 0; i < corpoCobra; i++) {
                if (i == 0) {
                    // Cabeça da cobra
                    g.setColor(new Color(34, 139, 34));
                    g.fillRect(eixoX[0], eixoy[0], TAMANHO_BLOCO, TAMANHO_BLOCO);
                } else {
                    // Corpo da cobra
                    g.setColor(new Color(144, 238, 144));
                    g.fillRect(eixoX[i], eixoy[i], TAMANHO_BLOCO, TAMANHO_BLOCO);
                }
            }

            g.setColor(new Color(0,0,0)            );
            g.setFont(new Font(FONTE, Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Pontuação: " + blocosComidos,
                    (LARGURA - metrics.stringWidth("Pontuação: " + blocosComidos)) / 2, g.getFont().getSize());
        } else {
            fimDeJogo(g);
        }
    }

    private void fimDeJogo(Graphics g) {
        g.setColor(new Color(178, 34, 34));
        g.setFont(new Font(FONTE, Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Pontuação: " + blocosComidos, (LARGURA - metrics.stringWidth("Pontuação: " + blocosComidos)) / 2,
                g.getFont().getSize());
        g.setFont(new Font(FONTE, Font.BOLD, 70));
        FontMetrics fonteEnd = getFontMetrics(g.getFont());
        g.drawString("\uD83D\uDE22 Game Over ", (LARGURA - fonteEnd.stringWidth("Game Over ")) / 2, ALTURA / 2);
        //
    }

    private void criarBloco() {
        blocosX = random.nextInt(LARGURA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
        blocosY = random.nextInt(ALTURA / TAMANHO_BLOCO) * TAMANHO_BLOCO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (estaRodando) {
            andar();
            alcancarBloco();
            validarLimites();
        }
        repaint();
    }

    private void validarLimites() {
        // Verfica se a cabeça bateu no corpo
        for (int i = corpoCobra; i > 0; i--) {
            if (eixoX[0] == eixoX[i] && eixoy[0] == eixoy[i]) {
                estaRodando = false;
                break;
            }
        }

        // Consulta se a cobra tocou na direita ou na esquerda
        if (eixoX[0] < 0 || eixoX[0] > LARGURA) {
            estaRodando = false;
        }

        // //Consulta se a cobra tocou no topo ou no final
        if (eixoy[0] < 0 || eixoy[0] > ALTURA) {
            estaRodando = false;
        }

        if (!estaRodando) {
            timer.stop();
        }
    }

    private void alcancarBloco() {
        if (eixoX[0] == blocosX && eixoy[0] == blocosY) {
            corpoCobra++;
            blocosComidos++;
            criarBloco();
        }
    }

    private void andar() {
        for (int i = corpoCobra; i > 0; i--) {
            eixoX[i] = eixoX[i - 1];
            eixoy[i] = eixoy[i - 1];
        }

        switch (direcao) {
            case 'C':
                eixoy[0] -= TAMANHO_BLOCO;
                break;
            case 'B':
                eixoy[0] += TAMANHO_BLOCO;
                break;
            case 'E':
                eixoX[0] -= TAMANHO_BLOCO;
                break;
            case 'D':
                eixoX[0] += TAMANHO_BLOCO;
                break;
            default:
                break;
        }
    }

    public class LeitorDeTeclas extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direcao != 'D') {
                        direcao = 'E';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direcao != 'E') {
                        direcao = 'D';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direcao != 'B') {
                        direcao = 'C';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direcao != 'C') {
                        direcao = 'B';
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
