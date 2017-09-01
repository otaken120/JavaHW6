//Project 6
//Name:Ken Deng
//9/9/2015



package HW6;

import java.io.Serializable;
import java.util.Date;

/**
 * The employee class indicates company's employee
 */
public abstract class Employee implements Serializable {

	//private static final long serialVersionUID = -8982829640222816544L;

	/**
	 * A login name, containing no spaces
	 */
	protected String login;

	/**
	 * Password is hashed as a byte array
	 */
	protected byte[] password;
	
	/**
	 * The base salary
	 */
	protected double salary;

	/**
	 * The employee's name
	 */
	protected String name;

	/**
	 * a final 5-digit number
	 */
	protected final int id;

	/**
	 * The create date
	 */
	protected Date created;

	/**
	 * a static integer variable used to generate next employee id
	 */
	protected static int nextId = 0;

	public Employee(String login, byte[] password, double salary, String name) {
		this.login = login;
		this.password = password;
		this.salary = salary;
		this.name = name;
		this.id = nextId++;
		this.created = new Date();
	}

	public Employee(String login, byte[] password, double salary, String name, int id,
			Date created) {
		this.login = login;
		this.password = password;
		this.salary = salary;
		this.name = name;
		this.id = id;
		this.created = created;
		if (this.id > nextId) {
			nextId = this.id;
		}
	}

	/**
	 * Set the new base salary
	 */
	public void setSalary(double salary) {
		this.salary = salary;
	}

	/**
	 * Returns the login name
	 */
	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public final byte[] getPassword() {
		return password;
	}
	
	public double getSalary() {
		return this.salary;
	}
	
	public String getCreated() {
		return this.created.toString();
	}
	/**
	 * Returns the unique id of employee
	 */
	public final int getId() {
		return id;
	}

	
	public static final int getNextId() {
		return nextId;
	}

	public static void setNextId(int ni) {
		Employee.nextId = ni;
	}

	public String toString() {
		return String.format("%05d\t%-16s\t%-8s\t%tF %tT\t%s", this.id,
				this.login, String.format("%6.2f", this.salary), this.created,
				this.created, this.name);
	}

	public String toDetailString() {
		return String.format(
				  "              ID#: %05d\n" 
				+ "Login Name: %s\n"
				+ "          Salary: %.2f\n" 
				+ "       Created: %tF %tT\n" 
				+ "   Full Name: %s",
				this.id, this.login, this.salary, this.created, this.created,
				this.name);
	}

	public abstract double getPay();
}

