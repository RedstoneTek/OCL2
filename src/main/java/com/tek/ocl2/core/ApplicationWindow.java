package com.tek.ocl2.core;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.tek.ocl2.core.detection.OriginVersion;
import com.tek.ocl2.core.launch.OriginStarter;
import com.tek.ocl2.core.launch.OriginStarter.LaunchMode;
import com.tek.ocl2.core.lib.FAQ;
import com.tek.ocl2.core.lib.OriginApi;
import com.tek.ocl2.core.lib.OriginPool;
import java.awt.Color;

public class ApplicationWindow {

	public static String originAdress = "127js7g1i7I0WlF2bCxAh1sYRUPM7cjqxnqigEl+A==D99F";
	
	private static ApplicationWindow instance;
	private JFrame frame;
	private OriginStarter starter;
	
	private JComboBox<OriginVersion> loadedVersions_combo;
	private JButton reloadVersions_btn;
	private JButton close_btn;
	private JLabel pools_lbl;
	private JLabel relay_lbl;
	private JLabel origin_lbl;
	private JList<OriginPool> pools_list;
	private DefaultListModel<OriginPool> pools_model;
	private JRadioButton regular_rd;
	private JRadioButton pool_rd;
	private JRadioButton poolhost_rd;
	private JCheckBox full_chckbx;
	private JCheckBox data_chckbx;
	private JTextArea console_text;
	
	public ApplicationWindow() {
		initialize();
	}

	private void initialize() {
		//Set default values of JFrame
		frame = new JFrame();
		frame.setBounds(100, 100, 552, 315);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setTitle("Origin Crypto-Launcher 2.0");
		frame.getContentPane().setLayout(null);
		
		frame.setResizable(false);
		
		//Register instance
		instance = this;
		
		//Change look and feel
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) { }
		
		//Initialise Managers
		starter = new OriginStarter();
		
		//Load Tab Layout
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 546, 286);
		frame.getContentPane().add(tabbedPane);
		
		JComponent basic_pane = new JPanel();
		basic_pane.setLayout(null);
		tabbedPane.addTab("Basic", basic_pane);
		
		JComponent console_pane = new JPanel();
		console_pane.setLayout(null);
		tabbedPane.addTab("Console", console_pane);
		
	    JComponent faq_pane = new JPanel();
	    faq_pane.setLayout(null);
	    tabbedPane.addTab("FAQ", faq_pane);
	    
	    JComponent info_pane = new JPanel();
	    info_pane.setLayout(null);
	    tabbedPane.addTab("Info", info_pane);
		
		//Load Basic Components
		loadedVersions_combo = new JComboBox<OriginVersion>();
		loadedVersions_combo.setBounds(104, 11, 202, 20);
		basic_pane.add(loadedVersions_combo);
		
		JLabel loadedVersions_lbl = new JLabel("Valid Versions:");
		loadedVersions_lbl.setBounds(10, 14, 120, 14);
		basic_pane.add(loadedVersions_lbl);
		
		pools_lbl = new JLabel("Pools");
		pools_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		pools_lbl.setFont(new Font("Tahoma", Font.PLAIN, 17));
		pools_lbl.setBounds(336, 14, 180, 14);
		basic_pane.add(pools_lbl);
		
		relay_lbl = new JLabel("Relay Status: ");
		relay_lbl.setBounds(10, 236, 134, 14);
		basic_pane.add(relay_lbl);
		
		origin_lbl = new JLabel("Origin Height: ");
		origin_lbl.setBounds(158, 236, 168, 14);
		basic_pane.add(origin_lbl);
		
		pools_model = new DefaultListModel<OriginPool>();
		
		pools_list = new JList<OriginPool>(pools_model);
		pools_list.setSize(0, 7);
		pools_list.setLocation(1, 1);
		pools_list.setFont(new Font("Tahoma", Font.PLAIN, 10));
		pools_list.setVisibleRowCount(-1);
		pools_list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		pools_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		basic_pane.add(pools_list);
		
		JScrollPane pools_scroll = new JScrollPane(pools_list);
		pools_scroll.setBounds(336, 39, 180, 190);
		basic_pane.add(pools_scroll);
		
		regular_rd = new JRadioButton("Regular Mode");
		regular_rd.setBounds(217, 92, 109, 23);
		regular_rd.setSelected(true);
		basic_pane.add(regular_rd);
		
		pool_rd = new JRadioButton("Pool Mode");
		pool_rd.setBounds(217, 118, 109, 23);
		basic_pane.add(pool_rd);
		
		poolhost_rd = new JRadioButton("Pool Host Mode");
		poolhost_rd.setBounds(217, 144, 109, 23);
		basic_pane.add(poolhost_rd);
		
		ButtonGroup mode_group = new ButtonGroup();
		mode_group.add(regular_rd);
		mode_group.add(pool_rd);
		mode_group.add(poolhost_rd);
		
		full_chckbx = new JCheckBox("Full Mode");
		full_chckbx.setBounds(217, 170, 97, 23);
		basic_pane.add(full_chckbx);
		
		data_chckbx = new JCheckBox("Collect Anonymous Data");
		data_chckbx.setFont(new Font("Tahoma", Font.PLAIN, 9));
		data_chckbx.setHorizontalAlignment(SwingConstants.LEFT);
		data_chckbx.setBounds(336, 232, 142, 23);
		basic_pane.add(data_chckbx);
		
		reloadVersions_btn = new JButton("Reload Versions");
		reloadVersions_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reloadVersions();
			}
		});
		reloadVersions_btn.setBounds(20, 42, 276, 23);
		basic_pane.add(reloadVersions_btn);
		
		JButton launch_btn = new JButton("Launch Origin Client");
		launch_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LaunchMode mode = null;
				
				if(regular_rd.isSelected()) mode = LaunchMode.REGULAR;
				if(pool_rd.isSelected()) mode = LaunchMode.POOL;
				if(poolhost_rd.isSelected()) mode = LaunchMode.POOLHOST;
				
				starter.startOrigin(mode, full_chckbx.isSelected(), (OriginVersion) loadedVersions_combo.getSelectedItem());
			}
		});
		launch_btn.setBounds(10, 111, 201, 23);
		basic_pane.add(launch_btn);
		
		close_btn = new JButton("Close Origin Client");
		close_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				starter.endOrigin();
			}
		});
		close_btn.setBounds(10, 147, 201, 23);
		basic_pane.add(close_btn);
	    
		JButton datahelp_btn = new JButton("?");
		datahelp_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "If you check this, it will make development for the launcher faster as I can get errors from users and thus fix bugs quicker. I do not collect any data about the mining itself.");
			}
		});
		datahelp_btn.setBounds(479, 232, 37, 23);
		basic_pane.add(datahelp_btn);
		
		//Load Console Components
		console_text = new JTextArea();
		console_text.setEditable(false);
		
		JScrollPane console_scroll = new JScrollPane(console_text);
		console_scroll.setBounds(10, 11, 521, 236);
		console_pane.add(console_scroll);
		
		//Load FAQ Components
		JTextArea faq_text = new JTextArea();
		faq_text.setEditable(false);
		
		loadFAQ(faq_text);
		
	    JScrollPane faq_scroll = new JScrollPane(faq_text);
	    faq_scroll.setBounds(10, 11, 521, 236);
	    faq_pane.add(faq_scroll);
		
	    //Load Info Components
	    JLabel origin_version = new JLabel("Latest Origin Version: " + OriginApi.getLatestVersion());
	    origin_version.setBounds(10, 11, 521, 14);
	    info_pane.add(origin_version);
	    
	    JLabel java_version = new JLabel("Your Java Version: " + System.getProperty("java.version"));
	    java_version.setBounds(10, 36, 521, 14);
	    info_pane.add(java_version);
	    
	    JButton btn_copyadress = new JButton("Copy Adress");
	    btn_copyadress.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		StringSelection selection = new StringSelection(originAdress);
	    		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    		clipboard.setContents(selection, selection);
	    	}
	    });
	    btn_copyadress.setBounds(380, 224, 151, 23);
	    info_pane.add(btn_copyadress);
	    
	    JLabel lblIWorkOn = new JLabel("I work on the launcher on my free time so please consider donating");
	    lblIWorkOn.setForeground(new Color(0, 100, 0));
	    lblIWorkOn.setBounds(10, 228, 521, 14);
	    info_pane.add(lblIWorkOn);
	    
		//Show UI
		frame.setVisible(true);
		
		//Load versions in same directory
		reloadVersions();
		
		//Load pools
		reloadPools();
		
		//Load status & height
		updateStatus();
		
		//Start updater
		Timer status_timer = new Timer(10000, new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
		    	updateStatus();
		    	reloadPools();
		    	reloadVersions();
		    }
		});
		
		status_timer.start();
		
		//Add close listener
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        starter.endOrigin();
		    }
		});
	}
	
	public void reloadVersions() {
		loadedVersions_combo.removeAllItems();
		
		for(OriginVersion version : OriginVersion.getVersions(new File("."))) {
			loadedVersions_combo.addItem(version);
		}
	}
	
	public void reloadPools() {
		pools_model.removeAllElements();
		
		for(OriginPool pool : OriginApi.getPools()) {
			pools_model.addElement(pool);
		}
	}
	
	public void updateStatus() {
		relay_lbl.setText("Relay Status: " + OriginApi.getRelayStatus().translate.toUpperCase());
    	origin_lbl.setText("Origin Height: " + OriginApi.getHeight());
	}
	
	public void loadFAQ(JTextArea area) {
		StringBuilder text = new StringBuilder();
		
		for(FAQ faq : OriginApi.getFaqs()) {
			text.append("Question: " + faq.question + "\n");
			text.append("Answer: " + faq.answer + "\n\n");
		}
		
		area.setText(text.toString());
	}
	
	public void print(String message) {
		console_text.append(message + "\n");
	}
	
	public boolean isAnonymousDataChecked() {
		return data_chckbx.isSelected();
	}
	
	public OriginStarter getOriginStarter() {
		return this.starter;
	}
	
	public static ApplicationWindow inst() {
		return instance;
	}
}
