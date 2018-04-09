package mainGUI;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import kafka.BaseMsg;
import kafka.ReturnMsg;
import kafka.SendMsgKafka;
import kafka.kafkaMsg;
import scala.annotation.StaticAnnotation;

public class KafkaMainGUI extends JFrame implements ActionListener {
	public JPanel jp1, jp2;
	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5, jlb6, jlb7, jlb8, jlb9, jlb10, jlb11, jlb12, jlb13, jlb14, jlb15, jlb16,
			jlb17;
	public JButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10;
	public JTextArea jta1, jta2;
	public JScrollPane jsp1;
	public JTextField tt1, tt2, tt3, tt4, tt5, tt6, tt7, tt8, tt9, tt10, tt11, tt12, tt13, tt14, tt15, tt16, tt17;
	public JRadioButton jrb1, jrb2, jrb3;
	public ButtonGroup bg1;
	public JProgressBar jpbProcessLoading1, jpbProcessLoading2;
	public SwingWorker<ReturnMsg, String> sw1;
	public SwingWorker<String, String> sw2;
	public SendMsgKafka smk = new SendMsgKafka();
	public static boolean isRunning = true;
	public static boolean isRunning2 = true;
	public JTabbedPane jtp = new JTabbedPane(JTabbedPane.TOP);
	public Utils utils = new Utils();
	public CountThread thread;
	public CountThread2 thread2;
	public BaseMsg baseMsg, baseMsg2;
	public boolean fileExit = false;

	public KafkaMainGUI() {
		init();
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// 国人牛逼主题，值得学习
				// 初始化字体
				InitGlobalFont(new Font("微软雅黑", Font.PLAIN, 11));
				// 设置本属性将改变窗口边框样式定义
				// 系统默认样式 osLookAndFeelDecorated
				// 强立体半透明 translucencyAppleLike
				// 弱立体感半透明 translucencySmallShadow
				// 普通不透明 generalNoTranslucencyShadow
				BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
				// 设置主题为BeautyEye
				try {
					org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 隐藏“设置”按钮
				UIManager.put("RootPane.setupButtonVisible", false);
				// 开启/关闭窗口在不活动时的半透明效果
				// 设置此开关量为false即表示关闭之，BeautyEye LNF中默认是true
				BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
				// 设置BeantuEye外观下JTabbedPane的左缩进
				// 改变InsetsUIResource参数的值即可实现
				UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(3, 20, 2, 20));
				// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

				new KafkaMainGUI();
			}
		});

	}

	// font
	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}

	private void init() {
		// 原理：首先判断exe所在目录是否存在隐藏的配置文件kafkamodel.properties，
		// 如果存在，则读取此配置文件，如果不存在则生成一个默认的隐藏的配置文件

		BaseMsg km = new BaseMsg();
		File f = new File("sendkafkamodel.properties");
		fileExit = utils.isFileExit(f);
		if (fileExit) {
			// 获取默认配置信息
			try {
				km = utils.getDefaultMsg(f, 0);
			} catch (Exception e1) {
				km = new BaseMsg();
				e1.printStackTrace();
			}
		}

		BaseMsg kmm = new BaseMsg();
		File ff = new File("consumerkafkamodel.properties");
		if (utils.isFileExit(ff)) {
			// 获取默认配置信息
			try {
				kmm = utils.getDefaultMsg(ff, 1);
			} catch (Exception e1) {
				kmm = new BaseMsg();
				e1.printStackTrace();
			}
		}

		ImageIcon i = new ImageIcon(getClass().getResource("kafka.png"));
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp1.setLayout(null);
		jp2.setLayout(null);

		jlb1 = new JLabel("IP：");
		jlb1.setBounds(20, 20, 100, 25);
		jp1.add(jlb1);
		tt1 = new JTextField();
		tt1.setBounds(80, 20, 150, 25);
		tt1.setText(km.getKafkaIp() != null ? km.getKafkaIp() : "");
		jp1.add(tt1);

		jlb2 = new JLabel("PORT：");
		jlb2.setBounds(250, 20, 100, 25);
		jp1.add(jlb2);
		tt2 = new JTextField();
		tt2.setBounds(320, 20, 150, 25);
		tt2.setText(km.getKafkaPort() != null ? km.getKafkaPort() : "");
		jp1.add(tt2);

		jlb3 = new JLabel("GroupId：");
		jlb3.setBounds(20, 60, 100, 25);
		jp1.add(jlb3);
		tt3 = new JTextField();
		tt3.setBounds(80, 60, 150, 25);
		tt3.setText(km.getKafkaGroupId() != null ? km.getKafkaGroupId() : "");
		jp1.add(tt3);

		jlb4 = new JLabel("Topic：");
		jlb4.setBounds(250, 60, 100, 25);
		jp1.add(jlb4);
		tt4 = new JTextField();
		tt4.setBounds(320, 60, 150, 25);
		tt4.setText(km.getKafkaTopic() != null ? km.getKafkaTopic() : "");
		jp1.add(tt4);

		jlb5 = new JLabel("条数：");
		jlb5.setBounds(20, 100, 100, 25);
		jp1.add(jlb5);
		tt5 = new JTextField();
		tt5.setBounds(80, 100, 150, 25);
		tt5.setText(km.getKafkaAccount() != null ? km.getKafkaAccount() : "");
		jp1.add(tt5);

		jlb6 = new JLabel("间隔时间：");
		jlb6.setBounds(250, 100, 100, 25);
		jp1.add(jlb6);
		tt6 = new JTextField();
		tt6.setBounds(320, 100, 120, 25);
		tt6.setText(km.getKafkaTimes() != null ? km.getKafkaTimes() : "");
		jp1.add(tt6);

		jlb7 = new JLabel("(ms)");
		jlb7.setBounds(440, 100, 35, 25);
		jp1.add(jlb7);

		jta1 = new JTextArea();
		jta1.setBounds(80, 180, 360, 250);
		jta1.setFont(new Font("宋体", Font.PLAIN, 20));
		jp1.add(jta1);
		if (!km.isKafkaIsDefault()) {
			jta1.setText(km.getKafkaMessage() != null ? km.getKafkaMessage() : "");
			jta1.setVisible(true);
		} else {
			jta1.setVisible(false);
		}

		jlb8 = new JLabel("Msg：");
		jlb8.setBounds(20, 140, 100, 25);
		jp1.add(jlb8);
		jrb1 = new JRadioButton("默认");
		jrb1.setBounds(80, 140, 60, 25);
		jrb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jrb1.isSelected()) {
					jta1.setVisible(false);
				} else {
					jta1.setVisible(true);
				}
			}
		});
		jp1.add(jrb1);
		jrb2 = new JRadioButton("自定义");
		jrb2.setBounds(170, 140, 80, 25);
		jrb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jrb2.isSelected()) {
					jta1.setVisible(true);
				} else {
					jta1.setVisible(false);
				}
			}
		});
		jp1.add(jrb2);
		bg1 = new ButtonGroup();
		bg1.add(jrb1);
		bg1.add(jrb2);

		// 如果没有文件则选择默认
		// 若km不存在其默认是false，稍作处理
		if (!fileExit) {
			jrb1.setSelected(true);
			jrb2.setSelected(false);
			jta1.setVisible(false);
		} else {
			jrb1.setSelected(km.isKafkaIsDefault());
			jrb2.setSelected(!km.isKafkaIsDefault());
		}

		button10 = new JButton("关于");
		button10.setBounds(20, 180, 50, 25);
		button10.addActionListener(this);
		jp1.add(button10);
		button10.setEnabled(true);

		jlb9 = new JLabel("Key：");
		jlb9.setBounds(250, 140, 100, 25);
		jp1.add(jlb9);
		tt7 = new JTextField();
		tt7.setBounds(295, 140, 60, 25);
		tt7.setText(km.getKafkaKey() != null ? km.getKafkaKey() : "");
		jp1.add(tt7);

		jlb10 = new JLabel("StartId：");
		jlb10.setBounds(360, 140, 100, 25);
		jp1.add(jlb10);
		tt8 = new JTextField();
		tt8.setBounds(410, 140, 60, 25);
		tt8.setText(km.getKafkaStartId() != null ? km.getKafkaStartId() : "");
		jp1.add(tt8);

		jpbProcessLoading1 = new JProgressBar();
		jpbProcessLoading1.setStringPainted(true); // 呈现字符串
		jpbProcessLoading1.setBounds(100, 440, 300, 25);
		jpbProcessLoading1.setMinimum(1);
		jpbProcessLoading1.setMaximum(100);
		jpbProcessLoading1.setVisible(false);
		jp1.add(jpbProcessLoading1);

		jlb11 = new JLabel();
		jlb11.setBounds(410, 440, 100, 25);
		jp1.add(jlb11);
		jlb11.setVisible(false);

		button1 = new JButton("发送");
		button1.setBounds(45, 480, 80, 25);
		button1.addActionListener(this);
		jp1.add(button1);
		button1.setEnabled(true);

		button2 = new JButton("中止");
		button2.setBounds(155, 480, 80, 25);
		button2.addActionListener(this);
		jp1.add(button2);
		button2.setVisible(true);
		button2.setEnabled(false);

		button3 = new JButton("重置");
		button3.setBounds(265, 480, 80, 25);
		button3.addActionListener(this);
		jp1.add(button3);
		button3.setVisible(true);
		button3.setEnabled(true);

		button8 = new JButton("保存模板");
		button8.setBounds(375, 480, 80, 25);
		button8.addActionListener(this);
		jp1.add(button8);

		// JP2
		jlb12 = new JLabel("IP：");
		jlb12.setBounds(20, 20, 100, 25);
		jp2.add(jlb12);
		tt9 = new JTextField();
		tt9.setBounds(80, 20, 150, 25);
		tt9.setText(kmm.getKafkaIp() != null ? kmm.getKafkaIp() : "");
		jp2.add(tt9);

		jlb13 = new JLabel("PORT：");
		jlb13.setBounds(250, 20, 100, 25);
		jp2.add(jlb13);
		tt10 = new JTextField();
		tt10.setBounds(320, 20, 150, 25);
		tt10.setText(kmm.getKafkaPort() != null ? kmm.getKafkaPort() : "");
		jp2.add(tt10);

		jlb14 = new JLabel("GroupId：");
		jlb14.setBounds(20, 60, 100, 25);
		jp2.add(jlb14);
		tt11 = new JTextField();
		tt11.setBounds(80, 60, 150, 25);
		tt11.setText(kmm.getKafkaGroupId() != null ? kmm.getKafkaGroupId() : "");
		jp2.add(tt11);

		jlb15 = new JLabel("Topic：");
		jlb15.setBounds(250, 60, 100, 25);
		jp2.add(jlb15);
		tt12 = new JTextField();
		tt12.setBounds(320, 60, 150, 25);
		tt12.setText(kmm.getKafkaTopic() != null ? kmm.getKafkaTopic() : "");
		jp2.add(tt12);

		// 消息区
		jta2 = new JTextArea();
		// 设置为不可编辑
		jta2.setEditable(false);
		jsp1 = new JScrollPane(jta2);
		jsp1.setBounds(20, 100, 450, 320);
		jsp1.setFont(new Font("宋体", Font.PLAIN, 18));
		jp2.add(jsp1);

		button7 = new JButton("清除缓存");
		button7.setBounds(45, 440, 80, 25);
		button7.addActionListener(this);
		jp2.add(button7);
		button7.setEnabled(true);

		jlb16 = new JLabel();
		jlb16.setBounds(410, 440, 100, 25);
		jp2.add(jlb16);
		jlb16.setVisible(false);

		jpbProcessLoading2 = new JProgressBar();
		jpbProcessLoading2.setStringPainted(true); // 呈现字符串
		jpbProcessLoading2.setBounds(160, 440, 200, 25);
		jpbProcessLoading2.setVisible(false);
		jp2.add(jpbProcessLoading2);

		button4 = new JButton("消费");
		button4.setBounds(45, 480, 80, 25);
		button4.addActionListener(this);
		jp2.add(button4);
		button4.setEnabled(true);

		button5 = new JButton("中止");
		button5.setBounds(155, 480, 80, 25);
		button5.addActionListener(this);
		jp2.add(button5);
		button5.setVisible(true);
		button5.setEnabled(false);

		button6 = new JButton("重置");
		button6.setBounds(265, 480, 80, 25);
		button6.addActionListener(this);
		jp2.add(button6);
		button6.setVisible(true);
		button6.setEnabled(true);

		button9 = new JButton("保存模板");
		button9.setBounds(375, 480, 80, 25);
		button9.addActionListener(this);
		jp2.add(button9);

		this.add(jtp);
		jtp.add("Send Message To Kafka", jp1);
		jtp.add("Get Message From Kafka", jp2);
		this.setTitle("Kafka");
		this.setSize(500, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(i.getImage());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听开始
		if (e.getSource().equals(button1)) {
			// get params
			baseMsg = getBaseMsg();
			// check
			int flag = checkParams(baseMsg);
			if (flag == 0) {
				button1.setEnabled(false);
				button2.setEnabled(true);
				button3.setEnabled(false);
				jpbProcessLoading1.setVisible(true);
				// jpbProcessLoading1.setIndeterminate(true); //
				// 设置进度条为不确定模式,默认为确定模式
				jpbProcessLoading1.setString("正在发送，请耐心等待...");
				jpbProcessLoading1.setValue(0);

				// start times
				KafkaMainGUI.isRunning = true;
				thread = new CountThread();
				thread.start();

				sw1 = new SwingWorker<ReturnMsg, String>() {
					// 此方法是耗时任务
					@Override
					protected ReturnMsg doInBackground() throws Exception {
						ReturnMsg returnMsg = null;
						returnMsg = smk.kafkaController(baseMsg);
						// 向前台实时刷新
						// jlb11.setVisible(true);
						// publish(String.valueOf(SendMsgKafka.TIMES));
						// System.out.println(SendMsgKafka.TIMES);
						return returnMsg;
					}

					// done
					protected void done() {
						ReturnMsg returnMsg = null;
						try {
							returnMsg = get();
						} catch (Exception e) {
							e.printStackTrace();
						}

						thread.stop();
						// jpbProcessLoading1.setIndeterminate(false);
						if (returnMsg != null) {
							if (returnMsg.isCompleted) {
								jpbProcessLoading1.setString("发送完成！");
								JOptionPane.showMessageDialog(null, "发送完成！共" + returnMsg.getComTimes() + "条", "提示消息",
										JOptionPane.WARNING_MESSAGE);
							} else {
								jpbProcessLoading1.setString("发送中止！");
								JOptionPane.showMessageDialog(null, "发送中止！共" + returnMsg.getComTimes() + "条", "提示消息",
										JOptionPane.WARNING_MESSAGE);
							}
							// jlb11.setText("Times:" +
							// returnMsg.getComTimes());
							// jlb11.setVisible(true);
						} else {
							jpbProcessLoading1.setString("发送失败！");
							JOptionPane.showMessageDialog(null, "发送失败！请检查！", "提示消息", JOptionPane.WARNING_MESSAGE);
						}
						button1.setEnabled(true);
						button2.setEnabled(false);
						button3.setEnabled(true);
					}

					// @Override
					// protected void process(List<String> times) {
					// if (SwingUtilities.isEventDispatchThread()) {
					// jlb11.setVisible(true);
					// for (String time : times) {
					// jlb11.setText("Times:" + time);
					// }
					// }
					// }

				};

				sw1.execute();
			} else if (flag == 1) {
				JOptionPane.showMessageDialog(null, "不能为空！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 2) {
				JOptionPane.showMessageDialog(null, "相关参数只能是数字！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 3) {
				JOptionPane.showMessageDialog(null, "IP不正确！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 4) {
				JOptionPane.showMessageDialog(null, "条数，间隔时间，初始ID超过范围！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 5) {
				JOptionPane.showMessageDialog(null, "Message格式不正确！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}

		}

		// 监听接收
		if (e.getSource().equals(button4)) {
			// get params
			baseMsg2 = getBaseMsg2();
			// check
			int flag = checkParams2(baseMsg2);
			if (flag == 0) {
				button4.setEnabled(false);
				button5.setEnabled(true);
				button6.setEnabled(false);
				jpbProcessLoading2.setVisible(true);
				jpbProcessLoading2.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
				jpbProcessLoading2.setString("正在接收，请耐心等待...");
				// start times
				KafkaMainGUI.isRunning2 = true;
				// thread2 = new CountThread2();
				// thread2.start();

				sw2 = new SwingWorker<String, String>() {
					int times2 = 0;

					// 此方法是耗时任务
					@Override
					protected String doInBackground() throws Exception {
						Properties props = smk.kafkaController2(baseMsg2);
						// smk.kafkaController3(baseMsg2);
						KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
						kafkaConsumer.subscribe(Arrays.asList(baseMsg2.getKafkaTopic()));
						while (true) {
							ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
							for (ConsumerRecord<String, String> record : records) {
								System.out.println("topic: " + record.topic() + " key: " + record.key() + " value: "
										+ record.value() + " partition: " + record.partition());
								// 实时发送到前台
								publish("topic: " + record.topic() + " key: " + record.key() + " value: "
										+ record.value() + " partition: " + record.partition());
							}
							if (!KafkaMainGUI.isRunning2) {
								// close connection
								kafkaConsumer.close();
								return "1";
							}
						}
					}

					// done
					protected void done() {
						String f = "0";
						try {
							f = get();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if ("1".equals(f)) {
							// thread2.stop();
							jpbProcessLoading2.setIndeterminate(false);
							jpbProcessLoading2.setString("中止接收！");
							button4.setEnabled(true);
							button5.setEnabled(false);
							button6.setEnabled(true);
						}
					}

					@Override
					protected void process(List<String> msgs) {
						if (SwingUtilities.isEventDispatchThread()) {
							jlb16.setVisible(true);
							for (String msg : msgs) {
								times2++;
								jlb16.setText("Times:" + times2);
								jta2.append(msg.toString() + "\r\n");
							}
						}
					}
				};
				sw2.execute();
			} else if (flag == 1) {
				JOptionPane.showMessageDialog(null, "不能为空！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 2) {
				JOptionPane.showMessageDialog(null, "相关参数只能是数字！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 3) {
				JOptionPane.showMessageDialog(null, "IP不正确！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}

		}

		// 监听中止
		if (e.getSource().equals(button2)) {
			KafkaMainGUI.isRunning = false;
			button1.setEnabled(true);
			button2.setEnabled(false);
			button3.setEnabled(true);
			thread.stop();
			// sw1.cancel(true);
		}

		// 监听中止
		if (e.getSource().equals(button5)) {
			KafkaMainGUI.isRunning2 = false;
			button4.setEnabled(true);
			button5.setEnabled(false);
			button6.setEnabled(true);
			// thread2.stop();
			// sw2.cancel(true);
			// sw1.cancel(true);
		}

		// 监听重置
		if (e.getSource().equals(button3)) {
			tt1.setText("");
			tt2.setText("");
			tt3.setText("");
			tt4.setText("");
			tt5.setText("");
			tt6.setText("");
			tt7.setText("");
			tt8.setText("");
			jta1.setText("");
			jlb11.setText("");
			jlb11.setVisible(false);
			jpbProcessLoading1.setVisible(false);
			jpbProcessLoading1.setValue(0);
			jrb1.setSelected(true);
			jta1.setVisible(false);
			button1.setEnabled(true);
			button2.setEnabled(false);
			button3.setEnabled(true);
		}
		// 监听重置
		if (e.getSource().equals(button6)) {
			tt9.setText("");
			tt10.setText("");
			tt11.setText("");
			tt12.setText("");
			jta2.setText("");
			jlb16.setText("");
			jlb16.setVisible(false);
			jpbProcessLoading2.setVisible(false);
			button4.setEnabled(true);
			button5.setEnabled(false);
			button6.setEnabled(true);
		}
		// 监听清除缓存
		if (e.getSource().equals(button7)) {
			jta2.setText("");
			jlb16.setText("");
		}

		// 监听关于
		if (e.getSource().equals(button10)) {
			String msg = "关于MsgKafka.exe：\r\n  1.该程序需要Java环境，若没有则下载复制Jre文件夹并改名“jre”，放置到MsgKafka.exe同级目录即可。\r\n  2.向kafka发送数据格式为简单json格式，默认自带id。\r\n  3.若条数为1则所有信息后缀不递增，若条数大于1则内容后缀递增，如“test”自动变成“test1”，“test2”。\r\n  4.常用信息设置好之后，可保存为模板，重启生效。";
			JOptionPane.showMessageDialog(null, msg, "提示消息", JOptionPane.WARNING_MESSAGE);
		}

		// 监听保存模板1
		if (e.getSource().equals(button8)) {
			// get params
			BaseMsg baseMsg3 = getBaseMsg();
			// check
			int flag = checkParams(baseMsg3);
			if (flag == 0) {
				// 存入配置文件
				try {
					if (utils.BaseMsgToPro(baseMsg3, 0)) {
						JOptionPane.showMessageDialog(null, "保存模板成功！重启生效！", "提示消息", JOptionPane.WARNING_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "保存模板失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "保存模板失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
				}
			} else if (flag == 1) {
				JOptionPane.showMessageDialog(null, "不能为空！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 2) {
				JOptionPane.showMessageDialog(null, "相关参数只能是数字！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 3) {
				JOptionPane.showMessageDialog(null, "IP不正确！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 4) {
				JOptionPane.showMessageDialog(null, "条数，间隔时间，初始ID超过范围！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 5) {
				JOptionPane.showMessageDialog(null, "Message格式不正确！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}
		// 监听保存模板2
		if (e.getSource().equals(button9)) {
			// get params
			BaseMsg baseMsg4 = getBaseMsg2();
			// check
			int flag = checkParams2(baseMsg4);
			if (flag == 0) {
				// 存入配置文件
				try {
					if (utils.BaseMsgToPro(baseMsg4, 1)) {
						JOptionPane.showMessageDialog(null, "保存模板成功！重启生效！", "提示消息", JOptionPane.WARNING_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "保存模板失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "保存模板失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
				}
			} else if (flag == 1) {
				JOptionPane.showMessageDialog(null, "不能为空！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 2) {
				JOptionPane.showMessageDialog(null, "相关参数只能是数字！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			} else if (flag == 3) {
				JOptionPane.showMessageDialog(null, "IP不正确！请重新填写！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private int checkParams(BaseMsg baseMsg) {
		if (!utils.checkBase1(baseMsg, 0)) {
			return 1;
		}
		if (!utils.checkBase2(baseMsg, 0)) {
			return 2;
		}
		if (!utils.checkIp(baseMsg.getKafkaIp())) {
			return 3;
		}
		if (!utils.checkAccTimeId(baseMsg)) {
			return 4;
		}
		if (!baseMsg.isKafkaIsDefault()) {
			if (!utils.checkMsg(baseMsg.getKafkaMessage())) {
				return 5;
			}
		}

		return 0;
	}

	private int checkParams2(BaseMsg baseMsg) {
		if (!utils.checkBase1(baseMsg, 1)) {
			return 1;
		}
		if (!utils.checkBase2(baseMsg, 1)) {
			return 2;
		}
		if (!utils.checkIp(baseMsg.getKafkaIp())) {
			return 3;
		}
		return 0;
	}

	// get base set
	private BaseMsg getBaseMsg() {
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setKafkaIp(tt1.getText().trim());
		baseMsg.setKafkaPort(tt2.getText().trim());
		baseMsg.setKafkaGroupId(tt3.getText().trim());
		baseMsg.setKafkaTopic(tt4.getText().trim());
		baseMsg.setKafkaAccount(tt5.getText().trim());
		baseMsg.setKafkaTimes(tt6.getText().trim());
		baseMsg.setKafkaKey(tt7.getText().trim());
		baseMsg.setKafkaIsDefault(jrb1.isSelected());
		baseMsg.setKafkaMessage(jta1.getText().trim());
		baseMsg.setKafkaStartId(tt8.getText().trim());
		return baseMsg;
	}

	// get base set
	private BaseMsg getBaseMsg2() {
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setKafkaIp(tt9.getText().trim());
		baseMsg.setKafkaPort(tt10.getText().trim());
		baseMsg.setKafkaGroupId(tt11.getText().trim());
		baseMsg.setKafkaTopic(tt12.getText().trim());
		return baseMsg;
	}

	// 计时线程
	private class CountThread extends Thread {
		private CountThread() {
			setDaemon(true);
		}

		@Override
		public void run() {
			jlb11.setVisible(true);
			while (KafkaMainGUI.isRunning) {
				jlb11.setText("Times:" + SendMsgKafka.TIMES);
				jpbProcessLoading1.setValue(100 * SendMsgKafka.TIMES / Integer.valueOf(baseMsg.getKafkaAccount()));
			}
		}

	}

	// 计时线程
	private class CountThread2 extends Thread {
		private CountThread2() {
			setDaemon(true);
		}

		@Override
		public void run() {
			// jlb11.setVisible(true);
			// while (KafkaMainGUI.isRunning) {
			// jlb11.setText("Times:" + SendMsgKafka.TIMES);
			// jpbProcessLoading1.setValue(100 * SendMsgKafka.TIMES /
			// Integer.valueOf(baseMsg.getKafkaAccount()));
			// }
		}

	}

}
