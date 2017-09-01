//Project 6
//Name:Ken Deng
//9/9/2015



package HW6;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;  // Label, TextField
import javafx.geometry.Pos;     // Pos.CENTER
import javafx.scene.layout.*;   // VBox and HBox
import javafx.scene.text.*;     // Font
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Payroll extends Application{
	private ArrayList<Employee> list = new ArrayList<Employee>();
	Scene sn_login;
	Scene sn_register;
	Scene sn_boss;
	Scene sn_employee;
	Scene sn_new;
	Scene sn_change;
	Scene sn_list;
	
	private Stage st;
	
	@Override
	public void start(Stage st) throws Exception {
		this.st = st;
		buildGui();
		readFile();
		//st.setScene(sn_login);
		st.show();
	}
	
	private void readFile() throws ClassNotFoundException {
		//PrintWriter pw;
		try {		
			FileInputStream fis = new FileInputStream("Employee.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			list = (ArrayList<Employee>) ois.readObject();
			Employee.setNextId(list.size());
			fis.close();
			ois.close();
			st.setScene(sn_login);
		}
		catch (FileNotFoundException e1) {	//first time create file
			try {
				File f = new File("Employee.txt");
				f.createNewFile();
				st.setScene(sn_register);
			}
			catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		catch(IOException e3) {
			e3.printStackTrace();
		}
		//return false;
	}
	
	private byte[] getNewPassword(String password, String checkpw) throws NoSuchAlgorithmException {
		MessageDigest md1 = MessageDigest.getInstance("SHA-256");
		md1.update(password.getBytes());
		byte[] byte_pw = md1.digest();
		MessageDigest md2 = MessageDigest.getInstance("SHA-256");
		md2.update(checkpw.getBytes());
		byte[] byte_check = md2.digest();
		if(!Arrays.equals(byte_pw, byte_check)) 
			return null;	
		return byte_pw;
	}
	
	private boolean comparePassword(byte[] realPassword, String inputPassword) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(inputPassword.getBytes());
		byte[] byte_pw = md.digest();
		if(Arrays.equals(byte_pw, realPassword))
			return true;
		else
			return false;
	}
	
	private void saveData() throws IOException {
		File fe = new File("Employee.txt");
		FileOutputStream fos = new FileOutputStream(fe);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(list);
		fos.close();
		oos.close();
	}
	
	public void buildGui() {
		//Font
		Font fn_label = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 18);
		Font fn_button = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 20);
		
		//----------------Build login scene & employee scene
		//Declaration
		Label lb_login_username = new Label("Username ");
		Label lb_login_password = new Label("Password ");
		TextField tf_login_username = new TextField();
		TextField tf_login_password = new TextField();
		Text txt_login_warning = new Text();
		Button bt_login_login = new Button("Login");
		HBox hb_login_username = new HBox();
		HBox hb_login_password = new HBox();
		HBox hb_login_buttons = new HBox();
		VBox vb_login = new VBox();
		
		Label lb_employee_id = new Label();
		Label lb_employee_username = new Label();
		Label lb_employee_name = new Label();
		Label lb_employee_salary = new Label();
		Label lb_employee_created = new Label();
		Button bt_employee_logout = new Button("Logout");
		VBox vb_employee = new VBox();
		//Set font
		lb_login_username.setFont(fn_label);
		lb_login_password.setFont(fn_label);
		tf_login_username.setFont(fn_label);
		tf_login_password.setFont(fn_label);
		bt_login_login.setFont(fn_button);
		lb_employee_id.setFont(fn_label);
		lb_employee_username.setFont(fn_label);
		lb_employee_name.setFont(fn_label);
		lb_employee_salary.setFont(fn_label);
		lb_employee_created.setFont(fn_label);
		bt_employee_logout.setFont(fn_button);
		//Build scene
		hb_login_username.getChildren().addAll(lb_login_username, tf_login_username);
		hb_login_username.setAlignment(Pos.CENTER);
		hb_login_username.setSpacing(20);
		hb_login_password.getChildren().addAll(lb_login_password, tf_login_password);
		hb_login_password.setAlignment(Pos.CENTER);
		hb_login_password.setSpacing(20);
		hb_login_buttons.getChildren().addAll(bt_login_login);
		hb_login_buttons.setAlignment(Pos.CENTER);
		hb_login_buttons.setSpacing(20);
		vb_login.getChildren().addAll(hb_login_username, hb_login_password, txt_login_warning, hb_login_buttons);
		vb_login.setAlignment(Pos.CENTER);
		vb_login.setSpacing(20);
		sn_login = new Scene(vb_login, 400, 300);
		vb_employee.getChildren().addAll(lb_employee_id, lb_employee_username, lb_employee_salary, 
				lb_employee_created, lb_employee_name, bt_employee_logout);
		vb_employee.setAlignment(Pos.CENTER);
		vb_employee.setSpacing(20);
		sn_employee = new Scene(vb_employee, 400, 300);
		//Set button action
		bt_login_login.setOnAction(e->{
			String login_name = tf_login_username.getText();
			String login_password = tf_login_password.getText();
			for(Employee k:list) {
				if(k == null || !k.getLogin().equals(login_name))
					continue;
				try {
					if(comparePassword(k.getPassword(),login_password)) {
						if(k.getId() == 0)
							st.setScene(sn_boss);
						else {
							st.setScene(sn_employee);
							lb_employee_id.setText(String.format("ID#: "+ k.getId()));
							lb_employee_username.setText(String.format("Login Name: "+ k.getLogin()));
							lb_employee_salary.setText(String.format("Salary: "+ k.getSalary()));
							lb_employee_created.setText(String.format("Created: "+ k.getCreated()));
							lb_employee_name.setText(String.format("Full name: "+ k.getName()));	
						}
					}
					else
						txt_login_warning.setText("Wrong password");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					return;
			}
		});
		
		bt_employee_logout.setOnAction(e->{
			Platform.exit();
		});
		
		//---------------Build register scene
		//Declaration
		Label lb_register_username = new Label("Enter username ");
		Label lb_register_name = new Label("My name is ");
		Label lb_register_password = new Label("Enter password ");
		Label lb_register_checkpw = new Label("Type it again ");
		Label lb_register_salary = new Label("Salary ");
		TextField tf_register_username = new TextField();
		TextField tf_register_name = new TextField();
		TextField tf_register_password = new TextField();
		TextField tf_register_checkpw = new TextField();
		TextField tf_register_salary = new TextField();
		ToggleGroup tg_register = new ToggleGroup();
		RadioButton rb_register_salaried = new RadioButton("Salaried");
		RadioButton rb_register_hourly = new RadioButton("Hourly");
		Text txt_register_warning = new Text();
		Button bt_register_ok = new Button("OK");
		HBox hb_register_username = new HBox();
		HBox hb_register_name = new HBox();
		HBox hb_register_password = new HBox();
		HBox hb_register_salary = new HBox();
		HBox hb_register_checkpw = new HBox();
		HBox hb_register_radiobuttons = new HBox();
		HBox hb_register_buttons = new HBox();
		VBox vb_register = new VBox();
		//Set font
		lb_register_username.setFont(fn_label);
		lb_register_name.setFont(fn_label);
		lb_register_password.setFont(fn_label);
		lb_register_checkpw.setFont(fn_label);
		lb_register_salary.setFont(fn_label);
		tf_register_username.setFont(fn_label);
		tf_register_name.setFont(fn_label);
		tf_register_password.setFont(fn_label);
		tf_register_checkpw.setFont(fn_label);
		tf_register_salary.setFont(fn_label);
		rb_register_salaried.setFont(fn_label);
		rb_register_hourly.setFont(fn_label);
		bt_register_ok.setFont(fn_button);
		//Build scene
		rb_register_salaried.setToggleGroup(tg_register);
		rb_register_hourly.setToggleGroup(tg_register);
		hb_register_username.getChildren().addAll(lb_register_username, tf_register_username);
		hb_register_username.setAlignment(Pos.CENTER);
		hb_register_username.setSpacing(20);
		hb_register_name.getChildren().addAll(lb_register_name, tf_register_name);
		hb_register_name.setAlignment(Pos.CENTER);
		hb_register_name.setSpacing(20);
		hb_register_password.getChildren().addAll(lb_register_password, tf_register_password);
		hb_register_password.setAlignment(Pos.CENTER);
		hb_register_password.setSpacing(20);
		hb_register_checkpw.getChildren().addAll(lb_register_checkpw, tf_register_checkpw);
		hb_register_checkpw.setAlignment(Pos.CENTER);
		hb_register_checkpw.setSpacing(20);
		hb_register_salary.getChildren().addAll(lb_register_salary, tf_register_salary);
		hb_register_salary.setAlignment(Pos.CENTER);
		hb_register_salary.setSpacing(20);
		hb_register_radiobuttons.getChildren().addAll(rb_register_salaried, rb_register_hourly);
		hb_register_radiobuttons.setAlignment(Pos.CENTER);
		hb_register_radiobuttons.setSpacing(20);
		hb_register_buttons.getChildren().addAll(bt_register_ok);
		hb_register_buttons.setAlignment(Pos.CENTER);
		hb_register_buttons.setSpacing(20);
		vb_register.getChildren().addAll(hb_register_username, hb_register_name, hb_register_password, 
				hb_register_checkpw, hb_register_salary, hb_register_radiobuttons, txt_register_warning, hb_register_buttons);
		vb_register.setAlignment(Pos.CENTER);
		vb_register.setSpacing(20);
		sn_register = new Scene(vb_register, 450, 500);
		//Set button action
		bt_register_ok.setOnAction(e->{
			String username = tf_register_username.getText();
			String name = tf_register_name.getText();
			String password = tf_register_password.getText();
			String checkpw = tf_register_password.getText();
			double bossSalary = Double.parseDouble(tf_register_salary.getText());
			if (username == "" || name == "" || password == "" || checkpw == "" //Check if the textFields are empty
					|| tf_register_salary.getText()=="" || (!rb_register_salaried.isSelected()&& !rb_register_hourly.isSelected())) {
				txt_register_warning.setText("Information can't be void");
				return;
			}
			byte[] bytepassword;
			try {
				bytepassword = getNewPassword(password, checkpw);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			if(bytepassword == null) {	//Check if the passwords are the same
				txt_register_warning.setText("Please enter password again");
				tf_register_password.setText("");
				tf_register_checkpw.setText("");
				return;
			}
			Employee.setNextId(0);
			if(rb_register_salaried.isSelected()) 
				list.add(new Salaried(username, bytepassword, bossSalary, name));
			else
				list.add(new Hourly(username, bytepassword, bossSalary, name));	
			try {
				saveData();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			st.setScene(sn_boss);
		});
		
		//---------------Build boss scene
		//Declaration
		Button bt_boss_new = new Button("Add New");
		Button bt_boss_change = new Button("Change Data");
		Button bt_boss_list = new Button("List Data");
		Button bt_boss_logout = new Button("Logout");
		HBox hb_boss = new HBox();
		VBox vb_boss = new VBox();
		//Set font
		bt_boss_new.setFont(fn_button);
		bt_boss_change.setFont(fn_button);
		bt_boss_list.setFont(fn_button);
		bt_boss_logout.setFont(fn_button);
		//Build scene
		hb_boss.getChildren().addAll(bt_boss_new, bt_boss_change, bt_boss_list);
		hb_boss.setAlignment(Pos.CENTER);
		hb_boss.setSpacing(10);
		vb_boss.getChildren().addAll(hb_boss, bt_boss_logout);
		vb_boss.setAlignment(Pos.CENTER);
		vb_boss.setSpacing(30);
		sn_boss = new Scene(vb_boss, 500, 300);
		//Set button action
		bt_boss_new.setOnAction(e->{
			st.setScene(sn_new);
		});
		
		bt_boss_change.setOnAction(e->{
			st.setScene(sn_change);
		});
		
		bt_boss_logout.setOnAction(e->{
			Platform.exit();
		});
		
		//----------Build create new employee scene
		//Declaration
		Label lb_new_username = new Label("Enter username ");
		Label lb_new_name = new Label("My name is ");
		Label lb_new_password = new Label("Enter password ");
		Label lb_new_checkpw = new Label("Type it again ");
		Label lb_new_salary = new Label("Salary ");
		TextField tf_new_username = new TextField();
		TextField tf_new_name = new TextField();
		TextField tf_new_password = new TextField();
		TextField tf_new_checkpw = new TextField();
		TextField tf_new_salary = new TextField();
		Text txt_new_warning = new Text();
		Button bt_new_ok = new Button("OK");
		Button bt_new_cancel = new Button("Cancel");
		ToggleGroup tg_new = new ToggleGroup();
		RadioButton rb_new_salaried = new RadioButton("Salaried");
		RadioButton rb_new_hourly = new RadioButton("Hourly");
		HBox hb_new_username = new HBox();
		HBox hb_new_name = new HBox();
		HBox hb_new_password = new HBox();
		HBox hb_new_checkpw = new HBox();
		HBox hb_new_salary = new HBox();
		HBox hb_new_radiobuttons = new HBox();
		HBox hb_new_buttons = new HBox();
		VBox vb_new = new VBox();
		//Set font
		lb_new_username.setFont(fn_label);
		lb_new_name.setFont(fn_label);
		lb_new_password.setFont(fn_label);
		lb_new_checkpw.setFont(fn_label);
		lb_new_salary.setFont(fn_label);
		tf_new_username.setFont(fn_label);
		tf_new_name.setFont(fn_label);
		tf_new_password.setFont(fn_label);
		tf_new_checkpw.setFont(fn_label);
		tf_new_salary.setFont(fn_label);
		bt_new_ok.setFont(fn_button);
		bt_new_cancel.setFont(fn_button);
		//Build scene
		rb_new_salaried.setToggleGroup(tg_new);
		rb_new_hourly.setToggleGroup(tg_new);
		hb_new_username.getChildren().addAll(lb_new_username, tf_new_username);
		hb_new_username.setAlignment(Pos.CENTER);
		hb_new_username.setSpacing(20);
		hb_new_name.getChildren().addAll(lb_new_name, tf_new_name);
		hb_new_name.setAlignment(Pos.CENTER);
		hb_new_name.setSpacing(20);
		hb_new_password.getChildren().addAll(lb_new_password, tf_new_password);
		hb_new_password.setAlignment(Pos.CENTER);
		hb_new_password.setSpacing(20);
		hb_new_checkpw.getChildren().addAll(lb_new_checkpw, tf_new_checkpw);
		hb_new_checkpw.setAlignment(Pos.CENTER);
		hb_new_checkpw.setSpacing(20);
		hb_new_salary.getChildren().addAll(lb_new_salary, tf_new_salary);
		hb_new_salary.setAlignment(Pos.CENTER);
		hb_new_salary.setSpacing(20);
		hb_new_radiobuttons.getChildren().addAll(rb_new_salaried, rb_new_hourly);
		hb_new_radiobuttons.setAlignment(Pos.CENTER);
		hb_new_radiobuttons.setSpacing(20);
		hb_new_buttons.getChildren().addAll(bt_new_ok, bt_new_cancel);
		hb_new_buttons.setAlignment(Pos.CENTER);
		hb_new_buttons.setSpacing(20);
		vb_new.getChildren().addAll(hb_new_username, hb_new_name, hb_new_password, hb_new_checkpw, 
				hb_new_salary, hb_new_radiobuttons, txt_new_warning, hb_new_buttons);
		vb_new.setAlignment(Pos.CENTER);
		vb_new.setSpacing(20);
		sn_new = new Scene(vb_new, 400, 450);
		//Set button action
		bt_new_ok.setOnAction(e->{
			String username = tf_new_username.getText();
			String name = tf_new_name.getText();
			String password = tf_new_password.getText();
			String checkpw = tf_new_password.getText();
			double salary = Double.parseDouble(tf_new_salary.getText());
			if (username == "" || name == "" || password == "" || checkpw == "" //Check if the textFields are empty
					|| tf_new_salary.getText()=="" || (!rb_new_salaried.isSelected()&& !rb_new_hourly.isSelected())) {
				txt_new_warning.setText("Information can't be void");
				return;
			}
			byte[] bytepassword;
			try {
				bytepassword = getNewPassword(password, checkpw);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			if(bytepassword == null) {	//Check if the passwords are the same
				txt_new_warning.setText("Please enter password again");
				tf_new_password.setText("");
				tf_new_checkpw.setText("");
				return;
			}
			if(rb_new_salaried.isSelected()) 
				list.add(new Salaried(username, bytepassword, salary, name));
			else
				list.add(new Hourly(username, bytepassword, salary, name));
			try {
				saveData();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			tf_new_username.setText("");
			tf_new_name.setText("");
			tf_new_password.setText("");
			tf_new_checkpw.setText("");
			tf_new_salary.setText("");
			txt_new_warning.setText("Add new employee suceed");
		});
		bt_new_cancel.setOnAction(e->{
			st.setScene(sn_boss);
		});
		
		//----------Build change employee scene
		//Declare
		Label lb_change_id = new Label("Enter employee's id ");
		Label lb_change_name = new Label("Name ");
		Label lb_change_salary = new Label("Salary");
		TextField tf_change_id = new TextField();
		TextField tf_change_name = new TextField();
		TextField tf_change_salary = new TextField();
		Button bt_change_name = new Button("Change name ");
		Button bt_change_salary = new Button("Change salary");
		Button bt_change_fire = new Button("Fire Employee");
		Button bt_change_cancel = new Button("Cancel");
		Text txt_change_warning = new Text("");
		HBox hb_change_id = new HBox();
		HBox hb_change_name = new HBox();
		HBox hb_change_salary = new HBox();
		HBox hb_change_buttons = new HBox();
		VBox vb_change = new VBox();
		//Set font
		lb_change_id.setFont(fn_label);
		lb_change_name.setFont(fn_label);
		lb_change_salary.setFont(fn_label);
		tf_change_id.setFont(fn_label);
		tf_change_name.setFont(fn_label);
		tf_change_salary.setFont(fn_label);
		bt_change_name.setFont(fn_button);
		bt_change_salary.setFont(fn_button);
		bt_change_fire.setFont(fn_button);
		bt_change_cancel.setFont(fn_button);
		//Build scene
		hb_change_id.getChildren().addAll(lb_change_id, tf_change_id);
		hb_change_id.setAlignment(Pos.CENTER);
		hb_change_id.setSpacing(20);
		hb_change_name.getChildren().addAll(lb_change_name, tf_change_name, bt_change_name);
		hb_change_name.setAlignment(Pos.CENTER);
		hb_change_name.setSpacing(10);
		hb_change_salary.getChildren().addAll(lb_change_salary, tf_change_salary, bt_change_salary);
		hb_change_salary.setAlignment(Pos.CENTER);
		hb_change_salary.setSpacing(10);
		hb_change_buttons.getChildren().addAll(bt_change_fire, bt_change_cancel);
		hb_change_buttons.setAlignment(Pos.CENTER);
		hb_change_buttons.setSpacing(20);
		vb_change.getChildren().addAll(hb_change_id, hb_change_name, hb_change_salary, txt_change_warning, hb_change_buttons);
		vb_change.setAlignment(Pos.CENTER);
		vb_change.setSpacing(20);
		sn_change = new Scene(vb_change, 500,400);
		//Set button action
		bt_change_name.setOnAction(e->{
			if(tf_change_id.getText() == "") {
				txt_change_warning.setText("Please enter the employee id");
				return;
			}
			int id = Integer.parseInt(tf_change_id.getText());
			if(list.size()<id) {
				txt_change_warning.setText("The employee id doesn't exist");
				return;
			}
			String name = tf_change_name.getText();
			if(name == "") {
				txt_change_warning.setText("The new name can't be void");
				return;
			}
			
			list.get(id).setName(name);
			try {
				saveData();
				txt_change_warning.setText("Change name suceed");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		bt_change_salary.setOnAction(e->{
			if(tf_change_id.getText() == "") {
				txt_change_warning.setText("Please enter the employee id");
				return;
			}
			int id = Integer.parseInt(tf_change_id.getText());
			if(list.size()<id) {
				txt_change_warning.setText("The employee id doesn't exist");
				return;
			}
			double salary = Double.parseDouble( tf_change_salary.getText());
			if(tf_change_name.getText() == "") {
				txt_change_warning.setText("The new name can't be void");
				return;
			}
			list.get(id).setSalary(salary);	
			try {
				saveData();
				txt_change_warning.setText("Change salary suceed");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		bt_change_fire.setOnAction(e->{
			if(tf_change_id.getText() == "") {
				txt_change_warning.setText("Please enter the employee id");
				return;
			}
			
			int id = Integer.parseInt(tf_change_id.getText());
			if(list.size() < id) {
				txt_change_warning.setText("The employee id doesn't exist");
				return;
			}
			if(id == 0) {
				txt_change_warning.setText("Can't fire boss");
				return;
			}
			list.remove(id);
			try {
				saveData();
				txt_change_warning.setText("Employee is deleted");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		bt_change_cancel.setOnAction(e->{
			st.setScene(sn_boss);
		});
		
		//--------Build List Scene
		//Declaration
		TextArea displayArea = new TextArea();
		Button bt_list_ok = new Button("OK");
		VBox vb_list = new VBox();
		//Set font & textarea size
		bt_list_ok.setFont(fn_button);
		displayArea.setMaxSize(650, 240);
		displayArea.setMinSize(650, 240);
		//Build scene
		vb_list.getChildren().addAll(displayArea, bt_list_ok);
		vb_list.setAlignment(Pos.CENTER);
		vb_list.setSpacing(20);
		sn_list = new Scene(vb_list, 700, 400);
		//Set action
		bt_boss_list.setOnAction(e->{
			st.setScene(sn_list);
			String display = "";
			for(Employee k:list) {
				display += k.toDetailString() + "\n\n";
			}
			displayArea.setText(display);
		});
		bt_list_ok.setOnAction(e->{
			st.setScene(sn_boss);
		});
		
		//---------Build Employee Scene
		
		//Build scene
		
	}
	
	public static void main(String[] args) {
		launch();
	}
}
