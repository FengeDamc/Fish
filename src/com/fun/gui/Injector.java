package com.fun.gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.fun.inject.Agent;
import com.fun.inject.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Injector extends JFrame {
    public boolean injected=false;
    public Injector() throws HeadlessException {
        super();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {

        }
        init();
        this.setSize(500,500);
        this.setVisible(true);


    }
    private void init(){
        setTitle("Fish"+ Agent.VERSION);
        setIconImage(new ImageIcon(getClass().getResource("/assets/texture/fishico.png")).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout());
        this.setBackground(Color.WHITE);
        JButton jb=new JButton("Inject");


        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 绘制背景图片
                Image backgroundImage = new ImageIcon(Injector.class.getResource("/assets/texture/injector1.png")).getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // 设置面板不透明
        panel.setOpaque(false);

        // 将面板添加到窗体
        this.add(panel);
        panel.add(jb);

        // 显示窗体

        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                jb.setVisible(false);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        for(int i=500;i>0;i--){
                            Injector.this.setSize(i,i);
                        }
                        Injector.this.setVisible(false);
                        Main.start();
                    }
                }.start();

            }
        });

    }
}
