package myn;
import myn.message;
import java.io.*;
import java.sql.*;
import java.util.*;
import jxl.write.*;
import jxl.*;



public class MynDatabase {
	//数据库参数
	public String _usr;
	public String _psd;
	//连接类对象
	Connection con;
	Statement stmt;
	int flag;
	public MynDatabase(String username,String password){
		_usr=username;
		_psd=password;
		flag=0;
	}
	/**
	 * 初始化连接
	 * @return
	 */
	public boolean initDriver(){
		try{
			Class.forName("com.mysql.jdbc.Driver"); 
			String url="jdbc:mysql://localhost:3306/message?characterEncoding=utf8&useSSL=true";     
			con=DriverManager.getConnection(url,_usr,_psd);
			flag=1;
			return true;
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	/**
	 * 关闭连接
	 * @return
	 */
	public boolean close(){
		try{
			if(flag>=1)
				con.close();
			if(flag>=2)
				stmt.close();
			flag=0;
			return true;
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	/**
	 * 插入一条数据
	 * @param a
	 * @return
	 */
	public boolean insert(message a){	
		try {
			if(flag>=1){
				stmt=con.createStatement();
				flag=2;
			}
			else return false;
		}
		catch (Exception e){
			System.out.println(e);
			return false;
		}		
		try {
			if(flag<2)
				return false;
			String sql = "INSERT INTO `list` (`from`, `to`, `msg`,`time`,`kind`) "
					+ "VALUES"+"(\""
					+a.getFrom()+"\", \""
					+a.getTo()+"\", \""
					+a.getMsg()+"\", \""
					+a.getTime()+"\", \""
					+a.getKind()+"\");";
			stmt.executeUpdate(sql);
			System.out.println("增加message成功");
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}
	/**
	 * 展示所有留言
	 */
	public void show(){
		try {
			if(flag>=1){
				stmt=con.createStatement();
				flag=2;
			}
			else return;
		}
		catch (Exception e){
			System.out.println(e);
			return;
		}		
		try {
			if(flag<2)
				return ;
			String sql = "SELECT * FROM `list`;";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("查询全表:");
			System.out.println("--------------------------------------------------------");
			int n =0;
			while(rs.next()){
				n++;
				message cur=new message(
						rs.getString("id"),rs.getString("from"),
						rs.getString("to"),rs.getString("msg"),
						rs.getString("kind"),rs.getString("time"));
				System.out.println(cur.toString());
			}
			System.out.println("共"+n+"行");
			System.out.println("--------------------------------------------------------");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	/**
	 * 查找选中留言
	 * @param from
	 * @param to
	 * @return
	 */
	public boolean select(String from,String to){
		try {
			if(flag>=1){
				stmt=con.createStatement();
				flag=2;
			}
			else return false;
		}
		catch (Exception e){
			System.out.println(e);
			return false;
		}		
		try {
			if(flag<2)
				return false;
			String sql = "SELECT * FROM `list` WHERE `from` =\""+ from +"\" and `to` =\""+to+"\";";
			ResultSet rs = stmt.executeQuery(sql);
			if (!rs.next()){
				System.out.println("查询失败!不存在该消息");
				return false;
			}
			else{
				int n =0;
				System.out.println("查询"+from+"给"+to+"的消息:");
				System.out.println("--------------------------------------------------------");
				do{
					n++;
					message cur=new message(
							rs.getString("id"),rs.getString("from"),
							rs.getString("to"),rs.getString("msg"),
							rs.getString("kind"),rs.getString("time"));
					System.out.println(cur.toString());
				}while(rs.next());
				System.out.println("共"+n+"行");
				System.out.println("--------------------------------------------------------");
				
			}
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}
	/**
	 * 删除选中留言
	 * @param id
	 * @return
	 */
	public boolean delete(int id){	
		try {
			if(flag>=1){
				stmt=con.createStatement();
				flag=2;
			}
			else return false;
		}
		catch (Exception e){
			System.out.println(e);
			return false;
		}		
		try {
			if(flag<2)
				return false;
			String sql = "SELECT * FROM `list` WHERE `id` ="+ id +";";
			ResultSet rs = stmt.executeQuery(sql);
			if (!rs.next()){
				System.out.println("删除失败!不存在该留言");
				return false;
			}	
			sql = "DELETE FROM `list` WHERE `id` = "+id+";";
			int result = stmt.executeUpdate(sql);
			System.out.println("删除了"+result+"行数据");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 修改选中留言
	 * @param id
	 * @param newone
	 * @return
	 */
	public boolean edit(int id,message newone){	
		try {
			if(flag>=1){
				stmt=con.createStatement();
				flag=2;
			}
			else return false;
		}
		catch (Exception e){
			System.out.println(e);
			return false;
		}		
		try {
			if(flag<2)
				return false;
			this.delete(id);
			this.insert(newone);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 删除数据表
	 * @param id
	 * @param newone
	 * @return
	 */
	public boolean clear(){	
		try {
			if(flag>=1){
				stmt=con.createStatement();
				flag=2;
			}
			else return false;
		}
		catch (Exception e){
			System.out.println(e);
			return false;
		}		
		try {
			if(flag<2)
				return false;
			String sql = "TRUNCATE list;";
			stmt.executeUpdate(sql);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 内部类：导出到Excel
	 * messagedb.xls
	 */
	public class TestDbToExcel {
				
	    public List<message> getAllByDb(){
	        List<message> list=new ArrayList<message>();
	        try {
	            String sql="select * from `list`;";
	            ResultSet rs= stmt.executeQuery(sql);
	            while (rs.next()) {
	            	message cur=new message(
							rs.getString("id"),rs.getString("from"),
							rs.getString("to"),rs.getString("msg"),
							rs.getString("kind"),rs.getString("time"));
	                list.add(cur);
	            }	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return list;
	    }
		
	    public void run() {
	        try {
	        	WritableWorkbook wwb = null;
	            // 创建可写入的Excel工作簿
	        	String fileName = "./messagedb.xls";
	        	File file=new File(fileName);
	        	if (!file.exists()) {
	        		file.createNewFile();
	        	}
	        	//以fileName为文件名来创建一个Workbook
	        	wwb = Workbook.createWorkbook(file);
	        	// 创建工作表
	        	WritableSheet ws = wwb.createSheet("test 1", 0);
	        	//查询数据库中所有的数据
	        	List<message> list= this.getAllByDb();
	        	//要插入到的Excel表格的行号，默认从0开始
	        	Label labelId= new Label(0, 0, "id(id)");
	        	Label labelFrom= new Label(1, 0, "发送方(from)");
	        	Label labelTo= new Label(2, 0, "接收方(to)");  
	        	Label labelMsg= new Label(3, 0, "内容(meesage)");
	        	Label labelKind= new Label(4, 0, "种类(kind)");
	        	Label labelTime= new Label(5, 0, "时间(time)");
	        	ws.addCell(labelId);
	        	ws.addCell(labelFrom);
	        	ws.addCell(labelTo);
	        	ws.addCell(labelMsg);
	        	ws.addCell(labelKind);
	        	ws.addCell(labelTime);
	        	
	        	for (int i = 0; i < list.size(); i++) {
	                   Label labelId_i= new Label(0, i+1, list.get(i).getId()+"");
	                   Label labelFrom_i= new Label(1, i+1, list.get(i).getFrom()+"");
	   	        	   Label labelTo_i= new Label(2, i+1,list.get(i).getTo()+"");
	   	        	   Label labelMsg_i= new Label(3, i+1,list.get(i).getMsg()+"");
	   	        	   Label labelKind_i= new Label(4, i+1,list.get(i).getKind()+"");
	   	        	   Label labelTime_i= new Label(5, i+1, list.get(i).getTime()+"");
	   	        	   ws.addCell(labelId_i);
		        	   ws.addCell(labelFrom_i);
		        	   ws.addCell(labelTo_i);
		        	   ws.addCell(labelMsg_i);
		        	   ws.addCell(labelKind_i);
		        	   ws.addCell(labelTime_i);
	               }    
	              //写进文档
	               wwb.write();
	              // 关闭Excel工作簿对象
	               wwb.close();      
	               System.out.println("导出到Excel成功,保存为messagedb.xls");
	        } catch (Exception e) {
	            e.printStackTrace();
	        } 
	    }
	}
	
	/**
	 * 内部类  将Excel 导入数据库
	 * messagedb.xls
	 */
	public class TestExcelToDb {
		
	    public boolean getAllByExcel(String file){
	        try {
	            System.out.println("读取Excel中...");
	            Workbook rwb=Workbook.getWorkbook(new File(file));
	            Sheet rs=rwb.getSheet("test 1");
	            int clos=rs.getColumns();//得到所有的列
	            int rows=rs.getRows();//得到所有的行
	            
	            System.out.println(clos+" rows:"+rows);
	            for (int i = 1; i < rows; i++) {
	                for (int j = 0; j < clos; j++) {
	                    //第一个是列数，第二个是行数
	                	message cur=new message(
	                			rs.getCell(j++, i).getContents(),
	                			rs.getCell(j++, i).getContents(),
	                			rs.getCell(j++, i).getContents(),
	                			rs.getCell(j++, i).getContents(),
	                			rs.getCell(j++, i).getContents(),
	                			rs.getCell(j++, i).getContents());
		               
	                    insert(cur);
	                }
	            }
	            System.out.println("读取的Excel完成");
	            return true;
	        } catch (Exception e) {
	            System.out.println("读取的Excel格式不正确! 读取中断!");
	            System.out.println(e);
	            return false;
	        } 
	    }
		
	    public void run(String fileName) {
	        getAllByExcel(fileName);
	    }
	}

}
