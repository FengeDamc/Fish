package com.fun.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.inject.Agent;
import com.fun.inject.Main;
import com.fun.inject.inject.InjectUtils;
import com.fun.inject.inject.wrapper.impl.MinecraftWrapper;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FishFrame extends JFrame {

    public DefaultMutableTreeNode settings;
    public JTree settingsTree;
    public JScrollPane jScrollPane;
    public JPanel jPanel=new JPanel(new GridLayout(0,1,5,5));

    public FishFrame(){
        super();
    }
    public static void init(){
        try {
            Agent.findClass("com.fun.gui.FishFrame").getDeclaredMethod("init0").invoke(null);

        } catch (Exception e) {
            init0();


            //e.printStackTrace();
        }
    }
    public static void init0(){
        Main.injector.setVisible(false);
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {

        }
        Agent.fishFrame = new FishFrame();
        Agent.fishFrame.initFrame();
    }



    public void initFrame(){

        settings=new DefaultMutableTreeNode("Settings");
        settingsTree=new JTree(settings);
        setTitle("Fish"+Agent.VERSION);
        setIconImage(new ImageIcon(getClass().getResource("/assets/texture/fishico.png")).getImage());
        System.out.println("Frame Init");
        Container container=getContentPane();
        for(Category c:Category.values()){
            DefaultMutableTreeNode ct=new DefaultMutableTreeNode(c.name());
            settings.add(ct);
            for (Module m: FunGhostClient.moduleManager.mods){
                if(m.category==c){
                    DefaultMutableTreeNode mt=new DefaultMutableTreeNode(m.getName());

                    ct.add(mt);

                }
            }
        }
        container.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
        //settingsTree.setBounds(0,30,200,309);
        settingsTree.setBackground(Color.darkGray);
        JScrollPane jScrollTree=new JScrollPane(settingsTree);

        //jScrollTree.setUI(new MultiScrollPaneUI());
        container.add(jScrollTree);

        JLabel label=new JLabel("Welcome to Fun's" +
                " FishGC ");
        label.setForeground(Color.YELLOW);
        label.setFont(new Font("微软雅黑",Font.BOLD+Font.ITALIC,30));
        jPanel.add(label);
        jScrollPane=new JScrollPane(jPanel);
        container.add(jScrollPane);
        //jPanel.setBounds(200,30,1000,1000);
        jPanel.setBackground(Color.darkGray);
        jPanel.setLayout(new VerticalFlowLayout());


        settingsTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getSource()==settingsTree&&e.getClickCount()==2){
                    SwingUtilities.updateComponentTreeUI(container);
                    //SwingUtilities.updateComponentTreeUI(jScrollTree);
                    int i=0;
                    String name = ((DefaultMutableTreeNode) ((JTree) e.getSource()).getLastSelectedPathComponent()).getUserObject().toString();
                    if (name == null) return;
                    Module m = FunGhostClient.moduleManager.getModule(name);
                    if (m == null) return;
                    for(Component jc:jPanel.getComponents()){
                        for(ComponentListener cl:jc.getComponentListeners()){
                            jc.removeComponentListener(cl);
                        }
                        jc.setVisible(false);
                        jPanel.remove(jc);
                    }
                    //jPanel.repaint();
                    if (m.getSettings() == null){
                        jPanel.setBackground(Color.darkGray);
                        return;
                    }
                    JPanel mp=new JPanel();
                    JLabel ml=new JLabel(m.getName());
                    ml.setFont(new Font("微软雅黑", Font.BOLD, 30));
                    JCheckBox mc=new JCheckBox();
                    mc.setSelected(m.isRunning());
                    mc.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            m.setRunning(mc.isSelected());
                        }
                    });
                    mp.add(ml);
                    mp.add(mc);
                    jPanel.add(mp);
                    for (Setting set : m.getSettings()) {
                        set.jComponents.clear();

                        ArrayList<JComponent> jComponents=new ArrayList<>();
                        if (set.isCheck()) {
                            i++;
                            JCheckBox jCheckBox=new JCheckBox(set.getName());
                            jCheckBox.setSelected(set.getValBoolean());
                            jPanel.add(jCheckBox);
                            jCheckBox.addChangeListener(new ChangeListener() {
                                @Override
                                public void stateChanged(ChangeEvent e) {
                                    set.setValBoolean(jCheckBox.isSelected());
                                }
                            });
                            jCheckBox.setBackground(Color.lightGray);
                            jCheckBox.setForeground(new Color(154, 130, 168));
                            jCheckBox.setBounds(new Rectangle(new Dimension(10,10)));
                            //jComponents.clear();
                            jComponents.add(jCheckBox);
                        }
                        if (set.isCombo()) {
                            i++;
                            JComboBox<String> comboBox = new JComboBox<String>();
                            JLabel jl = new JLabel(set.getName());
                            jl.setForeground(new Color(255, 209, 209));
                            jPanel.add(jl);
                            jPanel.add(comboBox);
                            ArrayList<String> options = set.getOptions();
                            int index =0;
                            for (int j = 0, optionsSize = options.size(); j < optionsSize; j++) {
                                String s = options.get(j);
                                comboBox.addItem(s);
                                if((s).equalsIgnoreCase(set.getValString())){
                                    //Agent.logger.info(s+j);
                                    index =j;
                                }
                            }
                            comboBox.setSelectedIndex(index);

                            comboBox.addItemListener(e12 -> {
                                try{
                                    MinecraftWrapper.get().addScheduledTask(()->{
                                        set.setValString(((String) comboBox.getSelectedItem()));
                                        //Agent.logger.info("itemChange"+(String) set.getValString());
                                    });
                                }
                                catch (Exception e1){
                                    set.setValString(((String) comboBox.getSelectedItem()));
                                }

                            });
                            comboBox.setBackground(Color.lightGray);
                            comboBox.setForeground(new Color(255, 130, 168));
                            comboBox.setBounds(new Rectangle(new Dimension(40,100)));
                            //jComponents.clear();
                            jComponents.add(comboBox);
                            jComponents.add(jl);


                        }
                        if (set.isSlider()) {
                            i++;
                            String dv = "" + Math.round(set.getValDouble() * 100D) / 100D;

                            JLabel jl = new JLabel(set.getName() + ":" + dv);
                            JSlider js = new JSlider(0, 100, (int) (set.getValDouble() / set.getMax() * 100));

                            js.setSize(50, 5);
                            jl.setSize(50, 5);
                            jl.setFont(new Font("微软雅黑", Font.BOLD, 25));


                            jPanel.add(jl);
                            jPanel.add(js);

                            js.addChangeListener(e1 -> {

                                set.setValDouble(js.getValue() / 100d * set.getMax());
                                String displayval = "" + Math.round(set.getValDouble() * 100D) / 100D;
                                jl.setText(set.getName() + ":" + displayval);

                                //Agent.logger.info(String.format("%.2f",js.getValue()/100*set.getMax()));
                            });
                            js.setBackground(Color.lightGray);
                            js.setForeground(new Color(154, 130, 168));
                            jl.setForeground(new Color(154, 130, 168));
                            js.setBounds(new Rectangle(new Dimension(100,5)));
                            //jComponents.clear();
                            jComponents.add(js);
                            jComponents.add(jl);



                        }
                        set.jComponents.addAll(jComponents);

                    }
                    if(i<=0){
                        jPanel.setBackground(Color.darkGray);
                    }


                }
            }
        });

        setVisible(true);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                ConfigModule.saveConfig();
                try {
                    InjectUtils.destroyClient();
                }
                catch (Exception ex){

                }
                System.exit(0);


            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });


        setSize(1000,618);
        container.setBackground(Color.darkGray);


        Thread updataUIThread= new Thread("Settings"){
            @Override
            public void run() {
                super.run();
                boolean keep=true;
                while (keep){
                    try {
                        for (Setting s : FunGhostClient.settingsManager.getSettings()) {

                            s.updateActiveState();

                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000/60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        updataUIThread.setPriority(Thread.MAX_PRIORITY);
        updataUIThread.start();
    }




}
