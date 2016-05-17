<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>文件搜索</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
  
  <jsp:useBean id="SearchBean" class ="org.gk.Javabean.SearchBean" scope="page"/>
  <jsp:setProperty property="*" name = "SearchBean"/>
  
  
  	
   	<table align="center" width="80%" cellpadding="0" cellspacing="0" border =1 >
		<tr>
			<td colspan="5">
				<form action="index.jsp" method="post">
					输入关键字:<input type="text" size="20" name="serachContent"/>
					<input type="submit" value="搜索"/>
				</form>
			</td>		
		</tr>
		
		<tr>
			<td>文件名称</td>
			<td colspan="2">文件内容</td>
			<td>文件路径</td>
			<td>最后修改</td>
			
		</tr>
		
			result: ${SearchBean.resultEntries}
		
	</table>

   	
   	
  </body>
</html>










