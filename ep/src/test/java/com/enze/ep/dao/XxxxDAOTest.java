package com.enze.ep.dao;

import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.enze.ep.entity.Xxxx;
import com.enze.ep.utils.UrlUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XxxxDAOTest {

	@Autowired
	XxxxDAO xxxxDAO;

	@Test
	public void test() {
		List<Xxxx> list = xxxxDAO.selectXxxxAll();
		// System.out.println(list.size());

		for (Xxxx x : list) {

			/*if (x.getName().length() < 10) {
				String s = UrlUtil.getURLEncoderString(x.getName());
				xxxxDAO.updateXxxx(x.getName(), s);
			}*/
	
				String regionName=x.getEncodename();
				if(!"".equals(regionName) && regionName!=null ) {
					String name=x.getName();
					xxxxDAO.updateXxxxflag(name, 4);
					//String url="http://www.cuncunle.com/village/search?regionName="+regionName+"&categoryType=village";
					//String url="http://www.cuncunle.com/village/search?regionName="+regionName+"&provinceId=521&cityId=1232&districtId=4181&categoryType=+village";
					//String url="http://www.cuncunle.com/village/search?regionName="+regionName+"&provinceId=521&cityId=1232&districtId=0&categoryType=+village";
					//String url="http://www.cuncunle.com/village/search?regionName="+regionName+"&provinceId=521&cityId=0&districtId=0&categoryType=+village";
					String url="http://www.cuncunle.com/village/search?regionName="+regionName+"&categoryType=village";
					System.out.println(url);
					getUrl(url,name);
					
				}

			
		}
		
		
		

	}

	public void getUrl(String url,String name) {

		Document doc;
		try {
			
			doc = Jsoup.connect(url).get();
			Elements spans=doc.select(".n_span");
			String resultname="";
			for(int i=0;i< spans.size();i++)
			{
				int n=i%2;
				if(n==0) {
					String _resultname=spans.get(i).text();
					break;
					/*if(_resultname.indexOf("临海")>=0) {
						resultname=_resultname;
						break;
					}*/
					/*if(_resultname.indexOf("台州")>=0) {
						resultname=_resultname;
						break;
					}*/
					/*if(_resultname.indexOf("浙江")>=0) {
						resultname=_resultname;
						break;
					}*/
				}
			}
			
			/*if("".equals(resultname)&&spans.size()>0 ) {
				resultname=spans.get(0).text();
			}*/
			if(!"".equals(resultname))
			xxxxDAO.updateXxxxres(name, resultname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
