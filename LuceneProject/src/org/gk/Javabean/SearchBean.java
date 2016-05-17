package org.gk.Javabean;

import java.util.List;

import org.gk.Adapter.ResultShower;
import org.gk.Entries.ResultEntry;
import org.gk.Service.IndexService;


public class SearchBean {
	
	private String serachContent ="";
	
	private String resultEntries ="1212";
	
	private static int id =0;
	
	public SearchBean() {
		super();
		System.out.println("it's me");
	}

	public String getSerachContent() {
		return serachContent;
	}

	public void setSerachContent(String serachContent) {
		this.serachContent = serachContent;
	}

	public String getResultEntries() {
		System.out.println("ser :" + serachContent);
		if(serachContent == "") return "";
//		try {
//			if(id == 0){
////				IndexService.addBookNotRepleace("F:\\my_paper\\别人家的论文\\test\\1.pdf", false);
////				IndexService.addBookNotRepleace("D:\\my_eclipse\\my_javaWEB1\\LuceneProject\\docTest\\达赖在日本声称解决“西藏问题”可仿效欧盟.docx", false);
////				IndexService.addBookNotRepleace("D:\\my_eclipse\\my_javaWEB1\\LuceneProject\\docTest\\日本跨境电商订单量急剧缩水 .docx",false);
////				IndexService.addBookNotRepleace("D:\\my_eclipse\\my_javaWEB1\\LuceneProject\\docTest\\Cheng Xu Yuan Mian Shi Bao Dian.pdf",false);
//			}
//		} catch (FileExistsEXception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		id++;
		System.out.println("serach :" +serachContent);
		List<ResultEntry> result=IndexService.queryBooks("日本");
		if (result == null) {
			return "";
		}
		return ResultShower.showResult(result);
	}

	public void setResultEntries(String resultEntries) {
		this.resultEntries = resultEntries;
	}

	
	
	
	
	
}
