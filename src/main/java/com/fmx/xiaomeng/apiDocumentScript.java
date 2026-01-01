package com.fmx.xiaomeng;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

public class apiDocumentScript {

    private static final String[] adjectives = {"开心的", "快乐的", "聪明的", "可爱的", "慷慨的", "勇敢的", "有趣的", "活泼的", "幸运的", "自信的"};
    private static final String[] nouns = {"小猫咪", "小狗狗", "小兔子", "小鸟儿", "小鱼儿", "小蜜蜂", "小蚂蚁", "小松鼠", "小熊猫", "小猪仔"};


    //     生成接口文档工具
    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        config.setProjectPath("D:\\Java\\IdeaProjects\\xiaomeng"); // root project path
        config.setProjectName("xiaomeng"); // project name
        config.setApiVersion("接口文档V8.0");       // api version
        config.setDocsPath("D:\\Java\\IdeaProjects\\xiaomeng"); // api docs target path
        config.setAutoGenerate(Boolean.TRUE);  // auto generate
        Docs.buildHtmlDocs(config); // execute to generate

//
//        Random random = new Random();
//        int adjectiveIndex1 = random.nextInt(adjectives.length);
//        int adjectiveIndex2 = random.nextInt(adjectives.length);
//        int nounIndex = random.nextInt(nouns.length);
//        String adjective1 = adjectives[adjectiveIndex1];
//        String adjective2 = adjectives[adjectiveIndex2];
//        String noun = nouns[nounIndex];
//        System.out.println(adjective1 + adjective2 + noun); ;




//        RandomHan han = new RandomHan();
//        System.out.print(han.getRandomHan());
    }

//
//    static class RandomHan {
//        private Random ran = new Random();
//        private final static int delta = 0x9fa5 - 0x4e00 + 1;
//
//        public char getRandomHan() {
//            return (char)(0x4e00 + ran.nextInt(delta));
//        }
//    }

}
