package com.huang.util;

import com.huang.entity.Location;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author timkobe
 */
public class DomXml {
    public static void main(String[] args) {


        try {
            SAXReader reader = new SAXReader();
//            URL url=new URL("C:/Users/timkobe/Downloads/英文.xml");
//            URL url1=new URL("C:\\Users\\timkobe\\Downloads\\中文.xml");
            // 创建saxReader对象
            // 通过read方法读取一个文件 转换成Document对象
            Document en = reader.read(new File("E:\\location\\英文源文件.xml"));
            Document cn = reader.read(new File("E:\\location\\中文所有多一个层级.xml"));
//            Document document = reader.read(url);
            System.out.println(cn);
            Element enode = en.getRootElement();
            List<Location> enlocations = new ArrayList<>();
           listNodes(enode, enlocations);
//            toJson(enlocations, Lang.EN_US);
            Element cnode = cn.getRootElement();
            List<Location> cnlocations = new ArrayList<>();
            listNodes(cnode, cnlocations);
//            elementMethod(enode);
//            toJson(cnlocations,Lang.ZH_CN);
//            toJson(cnlocations);
            System.out.println(enlocations);
            System.out.println(cnlocations);

                        /* 写入Txt文件 */
            File writename = new File("C:\\Users\\admin\\Desktop\\location_sql_new.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write("insert into location \r\n"); // \r\n即为换行
            out.write("(code,parent_code,name,full_name,lang,data_status,create_time) \r\n"); // \r\n即为换行
            out.write("values \r\n"); // \r\n即为换行

            for (Location location : cnlocations) {
                location.setDataStatus(1);
                String fullName = location.getFullName();
                if(fullName.indexOf("直辖市")>0){
                    fullName=fullName.replace("直辖市,","");
                }

                if(fullName.indexOf("特别行政区")>0){
                    fullName=fullName.replace("特别行政区,","");
                }

                if (fullName.indexOf("台湾省")>0){
                    fullName=fullName.replace("台湾省,","");
                }

                out.write("(\""+location.getCode()+"\",\""+location.getParentCode()+"\",\""+location.getName()+"\",\""+fullName+"\",\"zh-cn\",1,UNIX_TIMESTAMP(now(6))), \r\n"); // \r\n即为换行
            }


            for (Location location : enlocations) {
                location.setDataStatus(1);
                //全地址显示
                String fullName = location.getFullName();
                String[] split = fullName.split(",");
                String afterFullName=location.getFullName();
                if(split!=null && split.length>1){
                    afterFullName="";
                    for (int i = split.length-1; i>=0; i--) {
                        if(i==split.length-1){
                            afterFullName=split[i];
                        }else{
                            afterFullName=afterFullName+","+split[i];
                        }
                    }
                    System.out.println(afterFullName);
                }
                out.write("(\""+location.getCode()+"\",\""+location.getParentCode()+"\",\""+location.getName()+"\",\""+afterFullName+"\",\"en-us\",1,UNIX_TIMESTAMP(now(6))), \r\n"); // \r\n即为换行
//                break;
            }


            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 介绍Element中的element方法和elements方法的使用
     *
     * @param node
     */
    public static void elementMethod(Element node) {
        // 获取node节点中，子节点的元素名称为supercars的元素节点。
        Element Location = node.element("Location");
        Element CountryRegion = node.element("CountryRegion");
        // 获取supercars元素节点中，子节点为carname的元素节点(可以看到只能获取第一个carname元素节点)
        Element carname = CountryRegion.element("State");

        System.out.println(CountryRegion.getName() + "----" + carname.getText());

        // 获取supercars这个元素节点 中，所有子节点名称为carname元素的节点 。

        List<Element> carnames = CountryRegion.elements("carname");
        for (Element cname : carnames) {
            System.out.println(cname.getText());
        }

        // 获取supercars这个元素节点 所有元素的子节点。
        List<Element> elements = CountryRegion.elements();

        for (Element el : elements) {
            System.out.println(el.getText());
        }

    }

    /**
     * 遍历当前节点元素下面的所有(元素的)子节点
     *
     * @param node
     */
    public static List<Location> listNodes(Element node, List<Location> locations) {
        Location location = new Location();
        location.setCode("");
        location.setParentCode("");
        location.setLang("");
        location.setName("");
        System.out.println("当前节点的名称：：" + node.getName());
        // 获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        // 遍历属性节点
        //name
        getParent(node, location);

        getParentCode(node.getParent(), location);
        String name="";

        for (Attribute attr : list) {
            System.out.println(attr.getText() + "-----" + attr.getName() + "---" + attr.getValue());
            if (attr.getName().equals("Name")) {
                name = attr.getValue();
                location.setName(name);
            }
        }
        if (location.getCode() != null && !location.getCode().equals("") && list.size()>0) {
            locations.add(location);
        System.out.println("code:" + location.getCode());
        System.out.println("fullName:" + location.getFullName());
        System.out.println("parentCode:" + location.getParentCode());
        }
//        List content = node.content();
//        QName qName = node.getQName();
//        if (!(node.getTextTrim().equals(""))) {
//            System.out.println("文本内容：：：：" + node.getText());
//        }
//
//        // 当前节点下面子节点迭代器
        Iterator<Element> it = node.elementIterator();
//        // 遍历
        while (it.hasNext()) {
            // 获取某个子节点对象
            Element e = it.next();
            // 对子节点进行遍历
            listNodes(e, locations);
        }
//        // iterate through child elements of root
//        for (Iterator<Element> its = node.elementIterator(); it.hasNext(); ) {
//            Element element = its.next();
//            // do something
//        }
//
//        // iterate through child elements of root with element name "foo"
//        for (Iterator<Element> its = node.elementIterator("foo"); it.hasNext(); ) {
//            Element foo = its.next();
//            // do something
//        }
//
//        // iterate through attributes of root
//        for (Iterator<Attribute> its = node.attributeIterator(); it.hasNext(); ) {
//            Attribute attribute = its.next();
//            // do something
//        }
        return locations;
    }


    public static Location getParent(Element node, Location location) {
        if(node==null){
            return location;
        }
        String code = location.getCode() == null ? "" : location.getCode();
        String fullName = location.getFullName() == null ? "" : location.getFullName();
        Element parent = node.getParent();
        List<Attribute> attributes = node.attributes();
//        if (parent != null) {
            for (Attribute attr : attributes) {//追加name code
                if (attr.getName().equals("Name")) {
                    if (fullName.equals("")) {
                        fullName = attr.getValue();
                    } else {
                        fullName = attr.getValue() + "," + fullName;
                    }

                }
                if (attr.getName().equals("Code")) {
                    if (code.equals("")) {
                        code = attr.getValue();
                    } else {
                        code = attr.getValue() + "_" + code;
                    }
                }
            }
            location.setFullName(fullName);
            location.setCode(code);
            getParent(parent, location);
//        }

        return location;
    }

    public static Location getParentCode(Element parentNode, Location location) {
        if(parentNode==null){
            return location;
        }
        String code = location.getParentCode() == null ? "" : location.getParentCode();
        Element parent = parentNode.getParent();
        List<Attribute> attributes = parentNode.attributes();
//        if (parent != null) {
            for (Attribute attr : attributes) {//追加name code
                if (attr.getName().equals("Code")) {
                    if (code.equals("")) {
                        code = attr.getValue();
                    } else {
                        code = attr.getValue() + "_" + code;
                    }
                }
            }
            location.setParentCode(code);
            getParentCode(parent, location);
//        }

        return location;
    }

    public static void toJson(List<Location> locations ,String lang){
        List<LocationDto> locationDtos=new ArrayList<>(11049);
        LocationDto dto;
        for (Location location : locations) {
            dto=new LocationDto();
            BeanUtils.copyProperties(location,dto);
            locationDtos.add(dto);
        }

        for (LocationDto locationDto : locationDtos) {
            locationDto.setLang(null);
            List<Location> childs = locationDto.getChilds();
            if(childs==null){
                childs=new ArrayList<>();
                locationDto.setChilds(childs);
            }

            for (LocationDto locationDto1 : locationDtos) {
                    if(locationDto1.getParentCode().equals(locationDto.getCode())){
                        childs.add(locationDto1);
                    }
            }
        }
        List<LocationDto> collect = locationDtos.stream().filter(p -> StrUtil.isEmptyOrBlank(p.getParentCode())).collect(Collectors.toList());
        Map<String,Object> map=new HashMap<>();
        map.put(lang,collect);
        Gson gson=new Gson();
        String s = gson.toJson(map);


        try {
            File writename = new File("C:\\Users\\admin\\Desktop\\"+lang+".txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(s);
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toJson(List<Location> locations){
        Gson gson=new Gson();
        for (Location location : locations) {
            location.setLang(null);
            location.setFullName(null);
        }
        List<Location> collect = locations.stream().filter(p -> !StrUtil.isEmptyOrBlank(p.getParentCode())).collect(Collectors.toList());
        for (Location location : collect) {
            if(location.getParentCode().equals("1")){
                location.setParentCode(null);
            }
        }
        String s = gson.toJson(collect);
        try {
            File writename = new File("C:\\Users\\admin\\Desktop\\duoyiji.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(s);
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
