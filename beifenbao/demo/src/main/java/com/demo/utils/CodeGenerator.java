//package com.demo.utils;
//
//import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
//import com.baomidou.mybatisplus.core.toolkit.StringPool;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.InjectionConfig;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.po.TableInfo;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
///**
// * MyBatis-Plus 代码生成器
// */
//public class CodeGenerator {
//
//    /**
//     * 读取控制台内容
//     */
//    public static String scanner(String tip) {
//        Scanner scanner = new Scanner(System.in);
//        StringBuilder help = new StringBuilder();
//        help.append("请输入" + tip + ": ");
//        System.out.println(help.toString());
//        if (scanner.hasNext()) {
//            String ipt = scanner.next();
//            if (StringUtils.isNotBlank(ipt)) {
//                return ipt;
//            }
//        }
//        throw new MybatisPlusException("请输入正确的" + tip + "!");
//    }
//
//    public static void main(String[] args) {
//        // 代码生成器
//        AutoGenerator mpg = new AutoGenerator();
//
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir") + "/demo"; // 加上实际项目目录名
//        gc.setOutputDir(projectPath + "/src/main/java");
//        gc.setAuthor("xyh"); // 指定作者
//        gc.setOpen(false); // 设置生成后是否自动打开目录
//        gc.setFileOverride(true); // 设置文件存在时是否覆盖
//        gc.setSwagger2(true); // 设置是否生成Swagger注解
//        gc.setBaseResultMap(true); // 设置XML ResultMap
//        gc.setBaseColumnList(true); // 设置XML ColumnList
//        gc.setServiceName("%sService"); // 设置Service接口名称后缀，去掉Service接口首字母的I
//        mpg.setGlobalConfig(gc);
//
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl("jdbc:mysql://localhost:3306/grade-management?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC");
//        dsc.setSchemaName("public");
//        dsc.setDriverName("com.mysql.cj.jdbc.Driver"); // 数据库驱动类名，mysql8需要加上cj.
//        dsc.setUsername("root");
//        dsc.setPassword("root"); // 设置密码
//        mpg.setDataSource(dsc);
//
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        // pc.setModuleName(scanner("模块名")); // 无模块名则注释
//        pc.setParent("com.demo"); // 设置父包名
//        pc.setMapper("mapper"); // 设置Mapper接口所在的子包名
//        pc.setEntity("model"); // 设置实体类所在的子包名
//        pc.setController("controller"); // 设置Controller所在的子包名
//        pc.setService("service"); // 设置Service所在的子包名
//        mpg.setPackageInfo(pc);
//
//        // 自定义配置
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                // to do nothing
//            }
//        };
//
//        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
//        // 如果模板引擎是 velocity
//        // String templatePath = "/templates/mapper.xml.vm";
//
//        // 自定义输出配置
//        List<FileOutConfig> focList = new ArrayList<>();
//        // 自定义配置会被优先输出
//        focList.add(new FileOutConfig(templatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出文件名，如果你 Entity 设置了前后缀，此处注意 xml 的名称会跟着发生变化！！
//                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
//                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
//
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//
//        // 配置模板
//        TemplateConfig templateConfig = new TemplateConfig();
//
//        // 配置自定义输出模板
//        // 指定自定义模板路径，注意不要带上.ftl/.vm，会根据使用的模板引擎自动识别
//        // templateConfig.setEntity("templates/entity2.java");
//        // templateConfig.setService("templates/service.java");
//        // templateConfig.setController("templates/controller.java");
//
//        templateConfig.setXml(null);
//        mpg.setTemplate(templateConfig);
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        // strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!"); // 无则注释, 有则设置, 如BaseObject
//        strategy.setEntityLombokModel(true);
//        strategy.setRestControllerStyle(true);
//        // 公共父类
//        // strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!"); // 无则注释，有则设置，如BaseApiController
//        // 写于父类中的公共字段
//        // strategy.setSuperEntityColumns("id"); // 无则注释，有则设置
//        strategy.setInclude(scanner("表名,多个英文逗号分割").split(","));
//        strategy.setControllerMappingHyphenStyle(true);
//        strategy.setTablePrefix(pc.getModuleName() + "_"); // 去掉表的前缀
//        mpg.setStrategy(strategy);
//        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
//        mpg.execute();
//    }
//}