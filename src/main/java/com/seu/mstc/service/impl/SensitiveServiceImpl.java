package com.seu.mstc.service.impl;

import com.seu.mstc.service.SensitiveService;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveServiceImpl implements SensitiveService {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    @Override
    public void afterPropertiesSet(String name) {
        try{
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt;
            while((lineTxt = bufferedReader.readLine())!=null){
                addTree(lineTxt.trim());
            }
            reader.close();
        }catch (Exception e){
            logger.error("读取敏感词文件失败"+e.getMessage());
        }
    }

    private TrieNode rootNode = new TrieNode();
    private class TrieNode{
        private boolean end = false;
        private Map<Character,TrieNode> subNodes = new HashMap<>();
        public void addSubNode(Character key,TrieNode node){
            subNodes.put(key,node);
        }
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }
        public boolean isEndWord(){return end;}
        public void setEndWord(boolean end){
            this.end = end;
        }
    }
    private boolean isSymbol(char c){
        int ic =(int)c;
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic < 0x2E80||ic > 0x9FFF);
    }

    @Override
    public void addTree(String lineTxt) {
        TrieNode tempNode = rootNode;
        for(int i = 0;i<lineTxt.length();i++){
            if(tempNode.subNodes.containsKey(lineTxt.charAt(i))){
                tempNode = tempNode.subNodes.get(lineTxt.charAt(i));
            }else{
                TrieNode node = new TrieNode();
                if(i==lineTxt.length()-1) {
                    node.setEndWord(true);
                }
                tempNode.addSubNode(lineTxt.charAt(i),node);
                tempNode = tempNode.subNodes.get(lineTxt.charAt(i));
            }
        }
    }

    @Override
    public String filter(String text) {
        if(StringUtils.isBlank(text)){
            return text;
        }
        String replacement = "***";
        int position = 0;
        int begin = 0;
        TrieNode tempNode = rootNode;
        StringBuilder textfilt = new StringBuilder();
        while(position<text.length()){
            begin = position;
            tempNode = rootNode;
            Character tempWord = text.charAt(begin);
            while(!(tempNode.subNodes.get(tempWord)==null||tempNode.subNodes.get(tempWord).isEndWord())){
                tempNode = tempNode.subNodes.get(tempWord);
                begin++;
                tempWord = text.charAt(begin);
                while (isSymbol(tempWord)){
                    begin++;
                    tempWord = text.charAt(begin);
                }
            }
            if(tempNode.subNodes.get(tempWord)==null){
                textfilt.append(text.charAt(position));
                position++;
            }else if(tempNode.subNodes.get(tempWord).isEndWord()){
                textfilt.append(replacement);
                position = begin+1;
            }
        }

        return textfilt.toString();
    }

    //    public static void main(String[] argv){
//        SensitiveService s = new SensitiveService();
//        s.addTree("色情");
//        s.addTree("赌博");
//        System.out.println(s.filter(" 你 好 色 情,ffiff……赌博是不对的"));
//    }


}
