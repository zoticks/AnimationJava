/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gameee;

/**
 *
 * @author user
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

// Класс панели, на которой будет происходить анимация
public class Gameee extends JPanel implements ActionListener {

    // Изображения: два полноэкранных и одно для угла
    private BufferedImage img1, img2, cornerImg;

    // Прозрачность второго изображения (0.0 = невидимо, 1.0 = полностью видно)
    private float alpha = 0.0f;

    // Направление: true → второе изображение проявляется, false → исчезает
    private boolean fadeIn = true;

    // Таймер, управляющий обновлением анимации
    private Timer timer;

    // Флаг — показывать или скрывать угловое изображение
    private boolean showCorner = false;

    // Конструктор — загружает изображения и создаёт таймер
    public Gameee() {
        try {
            // Загружаем файлы изображений (должны лежать рядом с .java или указать полный путь)
            img1 = ImageIO.read(new File("Нетология.png"));
            img2 = ImageIO.read(new File("знания.jpg"));
            cornerImg = ImageIO.read(new File("реклама.png")); 

        } catch (IOException e) {
            System.out.println("Ошибка: изображения не найдены!");
            System.exit(1); // Закрываем программу, если загрузка не удалась
        }

        // Создаём таймер: каждые 40 миллисекунд будет вызывать метод actionPerformed()
        timer = new Timer(40, this);
    }

    // Метод перерисовки панели — здесь мы рисуем анимацию и угловую картинку
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Очищаем фон панели перед рисованием

        Graphics2D g2 = (Graphics2D) g; // приведение к Graphics2D для расширенных возможностей

        // 1) Рисуем первое изображение (полностью видно всегда)
        g2.drawImage(img1, 0, 0, getWidth(), getHeight(), null);

        // 2) Устанавливаем прозрачность второго изображения
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // 3) Рисуем второе изображение поверх первого с прозрачностью alpha
        g2.drawImage(img2, 0, 0, getWidth(), getHeight(), null);

        // Если флаг включен — рисуем угловую картинку
        if (showCorner) {

            int cornerWidth = 240;   // ширина углового изображения
            int cornerHeight = 120;  // высота
            int x = getWidth() - cornerWidth - 10;  // положение справа
            int y = getHeight() - cornerHeight - 10; // положение снизу

            // Восстанавливаем обычный режим прозрачности перед рисованием
            g2.setComposite(AlphaComposite.SrcOver);

            // Рисуем изображение в правом нижнем углу
            g2.drawImage(cornerImg, x, y, cornerWidth, cornerHeight, null);
        }
    }

    // Этот метод вызывается таймером каждые 40мс
    @Override
    public void actionPerformed(ActionEvent e) {

        // Управляем плавным изменением прозрачности
        if (fadeIn) {
            alpha += 0.02f; // увеличиваем прозрачность
            if (alpha >= 1.0f) fadeIn = false; // если достигли максимума — пора ослаблять
        } else {
            alpha -= 0.02f; // уменьшаем прозрачность
            if (alpha <= 0.0f) fadeIn = true; // если достигли минимума — снова усиливаем
        }

        repaint(); // Просим панель перерисоваться → создаёт эффект анимации
    }

    // Старт анимации — запускает таймер
    public void startAnimation() {
        timer.start();
    }

    // Стоп анимации — останавливает таймер
    public void stopAnimation() {
        timer.stop();
    }

    // Переключить отображение угловой картинки
    public void toggleCornerImage() {
        showCorner = !showCorner;
        repaint(); // перерисовать сразу, чтобы изменения были видны мгновенно
    }

    // Точка входа программы — создание окна и кнопок
    public static void main(String[] args) {

        JFrame frame = new JFrame("Gameee");

        Gameee panel = new Gameee(); // создаём нашу панель

        // Создаём кнопки
        JButton startBtn = new JButton("Старт");
        JButton stopBtn = new JButton("Стоп");
        JButton cornerBtn = new JButton("Показать/Скрыть картинку");

        // Назначаем кнопкам действия (при нажатии вызываются методы панели)
        startBtn.addActionListener(e -> panel.startAnimation());
        stopBtn.addActionListener(e -> panel.stopAnimation());
        cornerBtn.addActionListener(e -> panel.toggleCornerImage());

        // Панель для кнопок (расположены в ряд)
        JPanel controls = new JPanel();
        controls.add(startBtn);
        controls.add(stopBtn);
        controls.add(cornerBtn);

        // Размещаем компоненты в окне
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);   // панель с анимацией по центру
        frame.add(controls, BorderLayout.SOUTH); // кнопки снизу

        // Настройки окна
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // центрируем окно на экране
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true); // показываем окно
    }
}
