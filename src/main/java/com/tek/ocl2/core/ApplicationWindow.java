package com.tek.ocl2.core;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.tek.ocl2.core.detection.OriginVersion;
import com.tek.ocl2.core.launch.OriginStarter;
import com.tek.ocl2.core.launch.OriginStarter.LaunchMode;
import com.tek.ocl2.core.lib.OriginApi;
import com.tek.ocl2.core.lib.OriginPool;

public class ApplicationWindow {

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
	
	public ApplicationWindow() {
		initialize();
	}

	private void initialize() {
		//Set default values of JFrame
		frame = new JFrame();
		frame.setBounds(100, 100, 552, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setTitle("Origin Crypto-Launcher 2.0");
		frame.getContentPane().setLayout(null);
		
		//Register instance
		instance = this;
		
		//Change look and feel
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) { }
		
		//Initialise Managers
		starter = new OriginStarter();
		
		//Load UI Components
		loadedVersions_combo = new JComboBox<OriginVersion>();
		loadedVersions_combo.setBounds(104, 11, 202, 20);
		frame.getContentPane().add(loadedVersions_combo);
		
		JLabel loadedVersions_lbl = new JLabel("Valid Versions:");
		loadedVersions_lbl.setBounds(10, 14, 120, 14);
		frame.getContentPane().add(loadedVersions_lbl);
		
		pools_lbl = new JLabel("Pools");
		pools_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		pools_lbl.setFont(new Font("Tahoma", Font.PLAIN, 17));
		pools_lbl.setBounds(336, 14, 180, 14);
		frame.getContentPane().add(pools_lbl);
		
		relay_lbl = new JLabel("Relay Status: ");
		relay_lbl.setBounds(10, 236, 134, 14);
		frame.getContentPane().add(relay_lbl);
		
		origin_lbl = new JLabel("Origin Height: ");
		origin_lbl.setBounds(158, 236, 168, 14);
		frame.getContentPane().add(origin_lbl);
		
		pools_model = new DefaultListModel<OriginPool>();
		
		pools_list = new JList<OriginPool>(pools_model);
		pools_list.setSize(0, 7);
		pools_list.setLocation(1, 1);
		pools_list.setFont(new Font("Tahoma", Font.PLAIN, 10));
		pools_list.setVisibleRowCount(-1);
		pools_list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		pools_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		frame.getContentPane().add(pools_list);
		
		JScrollPane pools_scroll = new JScrollPane(pools_list);
		pools_scroll.setBounds(336, 39, 180, 190);
		frame.getContentPane().add(pools_scroll);
		
		regular_rd = new JRadioButton("Regular Mode");
		regular_rd.setBounds(217, 85, 109, 23);
		regular_rd.setSelected(true);
		frame.getContentPane().add(regular_rd);
		
		pool_rd = new JRadioButton("Pool Mode");
		pool_rd.setBounds(217, 111, 109, 23);
		frame.getContentPane().add(pool_rd);
		
		poolhost_rd = new JRadioButton("Pool Host Mode");
		poolhost_rd.setBounds(217, 137, 109, 23);
		frame.getContentPane().add(poolhost_rd);
		
		ButtonGroup mode_group = new ButtonGroup();
		mode_group.add(regular_rd);
		mode_group.add(pool_rd);
		mode_group.add(poolhost_rd);
		
		full_chckbx = new JCheckBox("Full Mode");
		full_chckbx.setBounds(217, 163, 97, 23);
		frame.getContentPane().add(full_chckbx);
		
		data_chckbx = new JCheckBox("Collect Anonymous Data");
		data_chckbx.setFont(new Font("Tahoma", Font.PLAIN, 9));
		data_chckbx.setHorizontalAlignment(SwingConstants.LEFT);
		data_chckbx.setBounds(336, 232, 142, 23);
		frame.getContentPane().add(data_chckbx);
		
		reloadVersions_btn = new JButton("Reload Versions");
		reloadVersions_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reloadVersions();
			}
		});
		reloadVersions_btn.setBounds(20, 42, 276, 23);
		frame.getContentPane().add(reloadVersions_btn);
		
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
		launch_btn.setBounds(10, 85, 201, 23);
		frame.getContentPane().add(launch_btn);
		
		close_btn = new JButton("Close Origin Client");
		close_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				starter.endOrigin();
			}
		});
		close_btn.setBounds(10, 111, 201, 23);
		frame.getContentPane().add(close_btn);
	    
		JButton datahelp_btn = new JButton("?");
		datahelp_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "If you check this, it will make development for the launcher faster as I can get errors from users and thus fix bugs quicker. I do not collect any data about the mining itself.");
			}
		});
		datahelp_btn.setBounds(479, 232, 37, 23);
		frame.getContentPane().add(datahelp_btn);
		
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
