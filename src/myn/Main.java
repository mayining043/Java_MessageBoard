package myn;
import java.util.Scanner;
import myn.MynDatabase.TestDbToExcel;
import myn.MynDatabase.TestExcelToDb;

/**
 * @author mayining
 * 留言信息管理系统
 */

public class Main {

	public static void main(String[] args) {
		System.out.println("尝试创建数据库连接中...");
		MynDatabase temp = new MynDatabase("root","123");
		System.out.println("数据库连接创建成功!");
		if(!temp.initDriver()){
			System.out.println("连接数据库失败!");
			System.exit(1);
		}
		System.out.println("数据库操作初始化成功!");
		System.out.println("欢迎来到留言信息管理系统!");
		System.out.println("操作说明:");
		System.out.println("0: 查询全表");
		System.out.println("1: 输入 from to msg kind 增加一条记录");
		System.out.println("2: 输入 id 删除一条记录");
		System.out.println("3: 输入 from to 0查询一条记录");
		System.out.println("4: 清空全表");
		System.out.println("5: 导出到excel表格");
		System.out.println("6: 输入excel文件目录导入到数据库");
		System.out.println("7: 退出");
		System.out.println();
		while (true){
			try{
				Scanner sc = new Scanner(System.in);
				int in = sc.nextInt();
				switch (in){
				case 7:
					System.out.println("程序已退出");
					sc.close();
					System.exit(0);	
					break;
				case 0:
					temp.show();
					break;
				case 1:
					System.out.println("请输入4行要插入的数据，每一行为 from to msg kind：\n");
					sc.nextLine();
					temp.insert(new message( sc.nextLine(),sc.nextLine(),
							sc.nextLine(),sc.nextLine()));
					break;
				case 2:
					temp.delete( sc.nextInt());
					break;
				case 3:
					temp.select( sc.next().trim(),sc.next().trim());
					break;
				case 4:
					if(temp.clear())
						System.out.println("清空成功！");
					else
						System.out.println("清空失败！");
					break;
				case 5:
					TestDbToExcel MysqltoExl = temp.new TestDbToExcel();
					MysqltoExl.run();
					break;
				case 6:
					TestExcelToDb ExltoMysql = temp.new TestExcelToDb();
					System.out.println("请输入要导入的excel文件名（位置为默认路径）：");
					String str=sc.next().trim();
					ExltoMysql.run(str);
					break;
				default:
					System.out.println("输入格式错误，请重新输入");
				}
			}catch (Exception e){
				System.out.println("输入格式错误，请重新输入");
			}
		}
	}
}
