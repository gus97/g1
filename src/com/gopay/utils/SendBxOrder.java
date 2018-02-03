package com.gopay.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.gopay.utils.hnbx.HNBX;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.gus.db.MD5;
import com.gus.rbc.EcooperationWebServiceService;
import com.gus.rbc.tb.EcooperationWebService;

public class SendBxOrder {

    private static Logger logger = Logger.getLogger("op");
    private static ApplicationContext context;

    // public static void main(String[] args) throws IOException {
    // String res = sendOrder2GetwaySync("2015060411081681790", 1, 5, 3);
    //
    // System.out.println(res);
    // }

    @SuppressWarnings({"rawtypes", "unused"})
    public static String sendOrder2Getway(final String BatchNo, int projectid, int companyid, int medid)
            throws IOException {
        context = new ClassPathXmlApplicationContext("com/gopay/spring_c3p0.xml");
        final JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");

        if (companyid == 5) {
            final StringBuffer buff_xml = new StringBuffer();
            buff_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            buff_xml.append("<PackageList><Package><Header>");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // ============================header===============================
            UUID uuid = UUID.randomUUID();

            jdbcTemplate.update("UPDATE picc_teamorder SET backreason = ? WHERE batchno = ?",
                    new Object[]{uuid.toString(), BatchNo});

            buff_xml.append("<UUID></UUID>");
            String Time = sdf.format(new Date());
            buff_xml.append("<Time></Time>");
            String Code = "20004";
            buff_xml.append("<Code>" + Code + "</Code>");
            String Asyn = "0";
            buff_xml.append("<Asyn>" + Asyn + "</Asyn>");
            String AsynReturnUrl = "";
            buff_xml.append("<AsynReturnUrl>" + AsynReturnUrl + "</AsynReturnUrl>");
            // 保险公司为渠道方分配
            String PartnerIdentifier = "hengsheng";
            buff_xml.append("<PartnerIdentifier>" + PartnerIdentifier + "</PartnerIdentifier>");
            buff_xml.append("<PartnerSerial></PartnerSerial>");
            buff_xml.append("</Header>");
            // ============================header===============================

            // 投保单信息（ApplyInfo）===========================================
            // 渠道类型
            String ChannelType = "01";
            String ChannelNum = "";
            String AgentNum = "";

            List<Map<String, Object>> key_info = jdbcTemplate.queryForList(
                    "SELECT toinscompany.agentcode FROM medcompany,toinscompany WHERE toinscompany.kind = 1 AND toinscompany.syscode = medcompany.code AND toinscompany.insid = ? AND medcompany.id = ?",
                    new Object[]{companyid, medid});
            if (key_info != null) {
                ChannelNum = (String) key_info.get(0).get("agentcode");
            }

            key_info = jdbcTemplate.queryForList(
                    "SELECT toinscompany.agentcode FROM medcompany,toinscompany WHERE toinscompany.kind = 4 AND toinscompany.syscode = medcompany.code AND toinscompany.insid = ? AND medcompany.id = ?",
                    new Object[]{companyid, medid});

            if (key_info != null) {
                AgentNum = (String) key_info.get(0).get("agentcode");
            }

            List<Map<String, Object>> order_res = jdbcTemplate.queryForList(
                    "SELECT * FROM picc_teamorder WHERE picc_teamorder.BatchNo = ? ", new Object[]{BatchNo});

            if (order_res == null || order_res.size() == 0) {
                return "订单错误,无此订单记录";
            }

            String ApplyDate = order_res.get(0).get("HandlerDate").toString().replace(".0", "");

            String EffectiveDate = order_res.get(0).get("CValiDate").toString().replace(".0", "");

            Float TotalPremium = (Float) order_res.get(0).get("realprem");

            buff_xml.append("<Request>");
            buff_xml.append("<IssueList>");
            buff_xml.append("<IssueItem>");

            // ==========================================================================================================
            int cnt = jdbcTemplate.queryForInt("SELECT count(1) FROM picc_teamorder WHERE batchno = ? ", // and
                    // status
                    // =
                    // 4
                    new Object[]{BatchNo});

            if (cnt == 0) {
                return "订单号或订单状态错误";
            }

            Map<String, Object> map1 = jdbcTemplate.queryForMap(
                    "SELECT GrpContNo,realprem FROM picc_teamorder WHERE batchno = ? ", // and
                    // status
                    // =
                    // 4
                    new Object[]{BatchNo});

            // ==============================================================================================================

            buff_xml.append("<ApplyInfo>");
            buff_xml.append("<ChannelType>" + ChannelType + "</ChannelType>");
            buff_xml.append("<ChannelNum>" + ChannelNum + "</ChannelNum>");
            buff_xml.append("<ChannelReginNum>HENGSHENG</ChannelReginNum>");
            buff_xml.append("<AgentNum>" + AgentNum + "</AgentNum>");
            buff_xml.append("<ApplyType>1</ApplyType>");
            buff_xml.append("<ApplyDate>" + ApplyDate + "</ApplyDate>");
            buff_xml.append("<EffectiveDate>" + EffectiveDate + "</EffectiveDate>");
            buff_xml.append("<TotalPremium>" + TotalPremium + "</TotalPremium>");

            // ===================================ProductList=======================================
            buff_xml.append("<ProductList>");
            List<Map<String, Object>> picc_teamproduct_res = jdbcTemplate.queryForList(
                    "SELECT * FROM insproject,picc_teamorder WHERE picc_teamorder.projectid=insproject.id AND picc_teamorder.BatchNo = ? ",
                    new Object[]{BatchNo});

            for (Iterator<Map<String, Object>> iterator = picc_teamproduct_res.iterator(); iterator.hasNext(); ) {
                Map<String, Object> map = iterator.next();
                int PeoplesInsu = (Integer) order_res.get(0).get("PeoplesInsu");
                float realprem = (Float) map.get("realprem");
                buff_xml.append("<ProductInfo>");
                buff_xml.append("<ProductCode>" + map.get("code") + "</ProductCode>");
                buff_xml.append("<InsuranceNum>1</InsuranceNum>");
                buff_xml.append("<InsurancePeriod></InsurancePeriod>");
                buff_xml.append("<PeriodPremium>" + jdbcTemplate
                        .queryForList("SELECT realprem/PeoplesInsu rs FROM picc_teamorder WHERE BatchNo = ? ",
                                new Object[]{BatchNo})
                        .get(0).get("rs") + "</PeriodPremium>");
                buff_xml.append("<InsuranceCategory>Y</InsuranceCategory>");
                buff_xml.append("<EffectiveDate>" + EffectiveDate + "</EffectiveDate>");
                buff_xml.append("<Frequency>0</Frequency>");
                buff_xml.append("<CalculationRule></CalculationRule>");
                buff_xml.append("<AfterDayEffective></AfterDayEffective>");
                buff_xml.append("<PaymentInstallments></PaymentInstallments>");
                buff_xml.append("<InsuranceCoverage></InsuranceCoverage>");
                buff_xml.append("<CoverPeriodType></CoverPeriodType>");
                buff_xml.append("<PremPeriodType></PremPeriodType>");
                buff_xml.append("<IsPackage>1</IsPackage>");
                buff_xml.append("</ProductInfo>");

            }
            buff_xml.append("</ProductList>");
            // ===================================ProductList=======================================

            // =====================================投保人信息=======================================
            buff_xml.append("<Applicant>");
            List<Map<String, Object>> picc_teamperson_res = jdbcTemplate.queryForList(
                    "SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured='00'",
                    new Object[]{BatchNo});
            if (picc_teamperson_res == null || picc_teamperson_res.size() == 0) {
                return "err:投保人信息为空";
            }

            for (Iterator<Map<String, Object>> iterator = picc_teamperson_res.iterator(); iterator.hasNext(); ) {
                Map<String, Object> map = iterator.next();
                buff_xml.append("<IDType>" + map.get("IDType") + "</IDType>");
                buff_xml.append("<ID>" + map.get("IDNo") + "</ID>");
                buff_xml.append("<Name>" + map.get("InsuredName") + "</Name>");
                buff_xml.append("<Sex>" + map.get("sex") + "</Sex>");
                buff_xml.append("<Birthday>" + map.get("birthday") + "</Birthday>");
                buff_xml.append("<CellPhoneNumber>" + map.get("phone") + "</CellPhoneNumber>");
                buff_xml.append("<Address></Address>");
                buff_xml.append("<Telephone></Telephone>");
                buff_xml.append("<Email></Email>");
            }
            buff_xml.append("</Applicant>");
            // =====================================投保人信息=======================================

            // =====================================被保人信息=======================================
            String sql_bbr = "SELECT COUNT(1) FROM picc_teamorder WHERE batchno = ? and isall = 1 ";
            int cnt_bbr = jdbcTemplate.queryForInt(sql_bbr, new Object[]{BatchNo});

            String sql = "SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? ";

            String IsApplicant = "1";

            if (cnt_bbr == 1) {
                IsApplicant = "0";
            } else {
                sql = "SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured <> '00'";
            }

            buff_xml.append("<InsuredInfo>");
            buff_xml.append("<IsApplicant>" + IsApplicant + "</IsApplicant>");
            buff_xml.append("<InsurantList>");
            List<Map<String, Object>> picc_teamperson_bb = jdbcTemplate.queryForList(sql, new Object[]{BatchNo});

            for (Iterator<Map<String, Object>> iterator = picc_teamperson_bb.iterator(); iterator.hasNext(); ) {
                Map<String, Object> map = iterator.next();
                buff_xml.append("<Insurant>");
                buff_xml.append("<IDType>" + map.get("IDType") + "</IDType>");
                buff_xml.append("<ID>" + map.get("IDNo") + "</ID>");
                buff_xml.append("<Name>" + map.get("InsuredName") + "</Name>");
                buff_xml.append("<Sex>" + map.get("sex") + "</Sex>");
                buff_xml.append("<Birthday>" + map.get("Birthday") + "</Birthday>");
                buff_xml.append("<CellPhoneNumber></CellPhoneNumber>");
                buff_xml.append("<Address></Address>");
                buff_xml.append("<Telephone></Telephone>");
                buff_xml.append("<Email></Email>");
                buff_xml.append(
                        "<InsurantApplicantRelation>" + map.get("RelationToInsured") + "</InsurantApplicantRelation>");
                buff_xml.append("<BenefitInfo>");
                buff_xml.append("<IsLegal>" + map.get("IsLegal") + "</IsLegal>");
                buff_xml.append("<BeneficiaryList>");

                // =====================================收益人信息=======================================
                sql = "SELECT * FROM picc_teambenefit WHERE batchno = ? AND ipersonid = ?";
                List<Map<String, Object>> picc_teambenefit = jdbcTemplate.queryForList(sql,
                        new Object[]{BatchNo, map.get("id")});

                if (picc_teambenefit == null || picc_teambenefit.size() == 0) {
                    buff_xml.append("<Beneficiary>");
                    buff_xml.append("<Index></Index>");
                    buff_xml.append("<IDType></IDType>");
                    buff_xml.append("<ID></ID>");
                    buff_xml.append("<Name></Name>");
                    buff_xml.append("<Sex></Sex>");
                    buff_xml.append("<Birthday></Birthday>");
                    buff_xml.append("<CellPhoneNumber></CellPhoneNumber>");
                    buff_xml.append("<Address></Address>");
                    buff_xml.append("<Telephone></Telephone>");
                    buff_xml.append("<Email></Email>");
                    buff_xml.append("<Relation></Relation>");
                    buff_xml.append("<Scale></Scale>");
                    buff_xml.append("</Beneficiary>");
                } else {
                    for (Iterator<Map<String, Object>> iterator2 = picc_teambenefit.iterator(); iterator2.hasNext(); ) {
                        Map<String, Object> map_teambenefit = (Map<String, Object>) iterator2.next();
                        buff_xml.append("<Beneficiary>");
                        buff_xml.append("<Index>" + map_teambenefit.get("Index1") + "</Index>");
                        buff_xml.append("<IDType>" + map_teambenefit.get("IDType") + "</IDType>");
                        buff_xml.append("<ID>" + map_teambenefit.get("IDNo") + "</ID>");
                        buff_xml.append("<Name>" + map_teambenefit.get("name") + "</Name>");
                        buff_xml.append("<Sex>" + map_teambenefit.get("sex") + "</Sex>");
                        buff_xml.append("<Birthday>" + map_teambenefit.get("Birthday") + "</Birthday>");
                        buff_xml.append("<CellPhoneNumber></CellPhoneNumber>");
                        buff_xml.append("<Address></Address>");
                        buff_xml.append("<Telephone></Telephone>");
                        buff_xml.append("<Email></Email>");
                        buff_xml.append("<Relation>00</Relation>");
                        buff_xml.append("<Scale>" + map_teambenefit.get("Scale") + "</Scale>");
                        buff_xml.append("</Beneficiary>");
                    }
                }

                buff_xml.append("</BeneficiaryList>");
                buff_xml.append("</BenefitInfo>");
                buff_xml.append("</Insurant>");
            }

            buff_xml.append("</InsurantList>");

            buff_xml.append("</InsuredInfo>");
            // =====================================投保人信息=======================================

            buff_xml.append("</ApplyInfo>");

            buff_xml.append("<Payment>");
            buff_xml.append("<PaymentId></PaymentId>");
            buff_xml.append("<BankSerial></BankSerial>");
            buff_xml.append("<PayBankCode></PayBankCode>");
            buff_xml.append("<PayBankAcount></PayBankAcount>");
            buff_xml.append("<PayBankUserId></PayBankUserId>");
            buff_xml.append("<ReciveBankCode></ReciveBankCode>");
            buff_xml.append("<ReciveBankAcount></ReciveBankAcount>");
            buff_xml.append("<ReciveBankUserId></ReciveBankUserId>");
            buff_xml.append("<ReciveBankUserName></ReciveBankUserName>");
            buff_xml.append("<PayType></PayType>");
            buff_xml.append("<PayTime>" + Time + "</PayTime>");
            buff_xml.append("<PayMoney>" + map1.get("realprem") + "</PayMoney>");
            buff_xml.append("<AccountDate></AccountDate>");
            buff_xml.append("</Payment>");

            buff_xml.append("<OrderInfo>");
            buff_xml.append("<OrderId>" + BatchNo + "</OrderId>");
            buff_xml.append("</OrderInfo>");

            buff_xml.append("<Flight>");
            buff_xml.append("<FltNO>" + order_res.get(0).get("Remark") + "</FltNO>");
            buff_xml.append("</Flight>");

            buff_xml.append("<OtherInfo>");
            buff_xml.append("<InsureNO>1</InsureNO>");
            buff_xml.append("<Plat>ZFB</Plat>");
            buff_xml.append("<ConIndProxyNO>hengsheng001</ConIndProxyNO>");

            buff_xml.append("</OtherInfo>");

            buff_xml.append("</IssueItem>");
            buff_xml.append("</IssueList>");
            buff_xml.append("</Request>");
            buff_xml.append("</Package>");
            buff_xml.append("</PackageList>");

            // 发送数据至保险网关
            logger.info("发送数据：" + buff_xml.toString() + "\n");

            // if(true) return "";

            jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?", new Object[]{5, BatchNo});

            List<Map<String, Object>> send_pa = jdbcTemplate.queryForList(
                    "SELECT interface, interkey FROM insproject WHERE id  = ?", new Object[]{projectid});
            if (send_pa == null || send_pa.size() == 0) {
                return "方案错误";
            }

            final String interface_url = (String) send_pa.get(0).get("interface");
            final String interkey = (String) send_pa.get(0).get("interkey");

            String result = CommsBx.sendInfo2BxGetWay(jdbcTemplate, interkey, buff_xml, interface_url, BatchNo);

            if (result == null || result.indexOf("err") != -1) {
                return result;
            }

            logger.info("得到返回结果：          " + result + "\n");
            logger.info(
                    "=========================================解析返回xml============================================\n");

            String bx_url = null;
            String FailReason = null;
            String PolicyNo = null;

            SAXReader saxReader = new SAXReader();
            try {
                Document document = saxReader.read(new ByteArrayInputStream(result.getBytes("UTF-8")));
                Element info = document.getRootElement();
                for (Iterator i = info.elementIterator(); i.hasNext(); ) {
                    Element info1 = (Element) i.next();
                    for (Iterator j = info1.elementIterator(); j.hasNext(); ) {
                        Element info2 = (Element) j.next();
                        for (Iterator k = info2.elementIterator(); k.hasNext(); ) {
                            Element info3 = (Element) k.next();
                            System.out.println(info3.getName() + ":" + info3.getText());
                            for (Iterator l = info3.elementIterator(); l.hasNext(); ) {
                                Element info4 = (Element) l.next();
                                System.out.println(info4.getName() + ":" + info4.getText());
                                for (Iterator m = info4.elementIterator(); m.hasNext(); ) {
                                    Element node = (Element) m.next();

                                    System.out.println(node.getName() + ":" + node.getText());

                                    if ("FailReason".equals(node.getName())) {
                                        FailReason = node.getText();
                                    }

                                    if ("PolicyNo".equals(node.getName())) {
                                        PolicyNo = node.getText();
                                    }
                                }
                            }
                        }
                    }
                    System.out.println();
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            logger.info("=========================================解析完成 ============================================\n");

            if (result.indexOf("<IsSuccess>1</IsSuccess>") == -1) {
                jdbcTemplate.update(
                        "INSERT INTO orderlog (batchno,front,behind,note,dealtime,companyid) VALUES (?,5,6,'出单失败',now(),?)",
                        new Object[]{BatchNo, companyid});
                return "出单失败，失败原因： " + FailReason;
            }
            jdbcTemplate.update("UPDATE picc_teamorder SET downurl = ? WHERE batchno = ?",
                    new Object[]{bx_url, BatchNo});
            jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?", new Object[]{7, BatchNo});
            jdbcTemplate.update(
                    "INSERT INTO orderlog (batchno,front,behind,note,dealtime,companyid) VALUES (?,5,7,'出单成功',now(),?)",
                    new Object[]{BatchNo, companyid});
            jdbcTemplate.update("UPDATE picc_teamorder SET grpcontno = ? where batchno = ?",
                    new Object[]{PolicyNo, BatchNo});
            String sin = JM.MD5("hengsheng#2015" + PolicyNo);
            String url = "http://app.lianlife.com/BS/Policy?channelNum=hengsheng&policyNo=" + PolicyNo + "&sign=" + sin;

            System.out.println();
            jdbcTemplate.update("UPDATE picc_teamorder SET downurl = ? where batchno = ?",
                    new Object[]{url, BatchNo});

            return "0000";
        } else if (companyid == 2) {
            return "0000";
        } else if (companyid == 7) {
            return ZJbx.zjbx(jdbcTemplate, BatchNo, companyid, projectid, medid);
        } else if (companyid == 8) {
            return DBbx.dbbx(jdbcTemplate, BatchNo, companyid, projectid, medid);
        } else {
            return "companyid err";
        }
    }

    @SuppressWarnings({"rawtypes", "unused"})
    public static String sendOrder2GetwaySync(final String BatchNo, int projectid, int companyid, int medid)
            throws IOException {

        context = new ClassPathXmlApplicationContext("com/gopay/spring_c3p0.xml");
        final JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");

        if (companyid == 5) {
            final StringBuffer buff_xml = new StringBuffer();
            buff_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            buff_xml.append("<PackageList><Package><Header>");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // ============================header===============================
            UUID uuid = UUID.randomUUID();

            jdbcTemplate.update("UPDATE picc_teamorder SET backreason = ? WHERE batchno = ?",
                    new Object[]{uuid.toString(), BatchNo});

            buff_xml.append("<UUID></UUID>");
            String Time = sdf.format(new Date());
            buff_xml.append("<Time></Time>");
            String Code = "20004";
            buff_xml.append("<Code>" + Code + "</Code>");
            String Asyn = "0";
            buff_xml.append("<Asyn>" + Asyn + "</Asyn>");
            String AsynReturnUrl = "";
            buff_xml.append("<AsynReturnUrl>" + AsynReturnUrl + "</AsynReturnUrl>");
            // 保险公司为渠道方分配
            String PartnerIdentifier = "hengsheng";
            buff_xml.append("<PartnerIdentifier>" + PartnerIdentifier + "</PartnerIdentifier>");
            buff_xml.append("<PartnerSerial></PartnerSerial>");
            buff_xml.append("</Header>");
            // ============================header===============================

            // 投保单信息（ApplyInfo）===========================================
            // 渠道类型
            String ChannelType = "01";
            String ChannelNum = "";
            String AgentNum = "";

            List<Map<String, Object>> key_info = jdbcTemplate.queryForList(
                    "SELECT toinscompany.agentcode FROM medcompany,toinscompany WHERE toinscompany.kind = 1 AND toinscompany.syscode = medcompany.code AND toinscompany.insid = ? AND medcompany.id = ?",
                    new Object[]{companyid, medid});
            if (key_info != null) {
                ChannelNum = (String) key_info.get(0).get("agentcode");
            }

            key_info = jdbcTemplate.queryForList(
                    "SELECT toinscompany.agentcode FROM medcompany,toinscompany WHERE toinscompany.kind = 4 AND toinscompany.syscode = medcompany.code AND toinscompany.insid = ? AND medcompany.id = ?",
                    new Object[]{companyid, medid});

            if (key_info != null) {
                AgentNum = (String) key_info.get(0).get("agentcode");
            }

            List<Map<String, Object>> order_res = jdbcTemplate.queryForList(
                    "SELECT * FROM picc_teamorder WHERE picc_teamorder.BatchNo = ? ", new Object[]{BatchNo});

            if (order_res == null || order_res.size() == 0) {
                return "订单错误,无此订单记录";
            }

            String ApplyDate = order_res.get(0).get("HandlerDate").toString().replace(".0", "");

            String EffectiveDate = order_res.get(0).get("CValiDate").toString().replace(".0", "");

            Float TotalPremium = (Float) order_res.get(0).get("realprem");

            buff_xml.append("<Request>");
            buff_xml.append("<IssueList>");
            buff_xml.append("<IssueItem>");

            // ==========================================================================================================
            int cnt = jdbcTemplate.queryForInt("SELECT count(1) FROM picc_teamorder WHERE batchno = ? ", // and
                    // status
                    // =
                    // 4
                    new Object[]{BatchNo});

            if (cnt == 0) {
                return "订单号或订单状态错误";
            }

            Map<String, Object> map1 = jdbcTemplate.queryForMap(
                    "SELECT GrpContNo,realprem FROM picc_teamorder WHERE batchno = ? ", // and
                    // status
                    // =
                    // 4
                    new Object[]{BatchNo});

            // ==============================================================================================================

            buff_xml.append("<ApplyInfo>");
            buff_xml.append("<ChannelType>" + ChannelType + "</ChannelType>");
            buff_xml.append("<ChannelNum>" + ChannelNum + "</ChannelNum>");
            buff_xml.append("<ChannelReginNum>HENGSHENG</ChannelReginNum>");
            buff_xml.append("<AgentNum>" + AgentNum + "</AgentNum>");
            buff_xml.append("<ApplyType>1</ApplyType>");
            buff_xml.append("<ApplyDate>" + ApplyDate + "</ApplyDate>");
            buff_xml.append("<EffectiveDate>" + EffectiveDate + "</EffectiveDate>");
            buff_xml.append("<TotalPremium>" + TotalPremium + "</TotalPremium>");

            // ===================================ProductList=======================================
            buff_xml.append("<ProductList>");
            List<Map<String, Object>> picc_teamproduct_res = jdbcTemplate.queryForList(
                    "SELECT * FROM insproject,picc_teamorder WHERE picc_teamorder.projectid=insproject.id AND picc_teamorder.BatchNo = ? ",
                    new Object[]{BatchNo});

            for (Iterator<Map<String, Object>> iterator = picc_teamproduct_res.iterator(); iterator.hasNext(); ) {
                Map<String, Object> map = iterator.next();
                int PeoplesInsu = (Integer) order_res.get(0).get("PeoplesInsu");
                float realprem = (Float) map.get("realprem");
                buff_xml.append("<ProductInfo>");
                buff_xml.append("<ProductCode>" + map.get("code") + "</ProductCode>");
                buff_xml.append("<InsuranceNum>1</InsuranceNum>");
                buff_xml.append("<InsurancePeriod></InsurancePeriod>");
                buff_xml.append("<PeriodPremium>" + jdbcTemplate
                        .queryForList("SELECT realprem/PeoplesInsu rs FROM picc_teamorder WHERE BatchNo = ? ",
                                new Object[]{BatchNo})
                        .get(0).get("rs") + "</PeriodPremium>");
                buff_xml.append("<InsuranceCategory>Y</InsuranceCategory>");
                buff_xml.append("<EffectiveDate>" + EffectiveDate + "</EffectiveDate>");
                buff_xml.append("<Frequency>0</Frequency>");
                buff_xml.append("<CalculationRule></CalculationRule>");
                buff_xml.append("<AfterDayEffective></AfterDayEffective>");
                buff_xml.append("<PaymentInstallments></PaymentInstallments>");
                buff_xml.append("<InsuranceCoverage></InsuranceCoverage>");
                buff_xml.append("<CoverPeriodType></CoverPeriodType>");
                buff_xml.append("<PremPeriodType></PremPeriodType>");
                buff_xml.append("<IsPackage>1</IsPackage>");
                buff_xml.append("</ProductInfo>");

            }
            buff_xml.append("</ProductList>");
            // ===================================ProductList=======================================

            // =====================================投保人信息=======================================
            buff_xml.append("<Applicant>");
            List<Map<String, Object>> picc_teamperson_res = jdbcTemplate.queryForList(
                    "SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured='00'",
                    new Object[]{BatchNo});
            if (picc_teamperson_res == null || picc_teamperson_res.size() == 0) {
                return "err:投保人信息为空";
            }

            for (Iterator<Map<String, Object>> iterator = picc_teamperson_res.iterator(); iterator.hasNext(); ) {
                Map<String, Object> map = iterator.next();
                buff_xml.append("<IDType>" + map.get("IDType") + "</IDType>");
                buff_xml.append("<ID>" + map.get("IDNo") + "</ID>");
                buff_xml.append("<Name>" + map.get("InsuredName") + "</Name>");
                buff_xml.append("<Sex>" + map.get("sex") + "</Sex>");
                buff_xml.append("<Birthday>" + map.get("birthday") + "</Birthday>");
                buff_xml.append("<CellPhoneNumber>" + map.get("phone") + "</CellPhoneNumber>");
                buff_xml.append("<Address></Address>");
                buff_xml.append("<Telephone></Telephone>");
                buff_xml.append("<Email></Email>");
            }
            buff_xml.append("</Applicant>");
            // =====================================投保人信息=======================================

            // =====================================被保人信息=======================================
            String sql_bbr = "SELECT COUNT(1) FROM picc_teamorder WHERE batchno = ? and isall = 1 ";
            int cnt_bbr = jdbcTemplate.queryForInt(sql_bbr, new Object[]{BatchNo});

            String sql = "SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? ";

            String IsApplicant = "1";

            if (cnt_bbr == 1) {
                IsApplicant = "0";
            } else {
                sql = "SELECT * FROM picc_teamperson WHERE picc_teamperson.BatchNo  = ? and RelationToInsured <> '00'";
            }

            buff_xml.append("<InsuredInfo>");
            buff_xml.append("<IsApplicant>" + IsApplicant + "</IsApplicant>");
            buff_xml.append("<InsurantList>");
            List<Map<String, Object>> picc_teamperson_bb = jdbcTemplate.queryForList(sql, new Object[]{BatchNo});

            for (Iterator<Map<String, Object>> iterator = picc_teamperson_bb.iterator(); iterator.hasNext(); ) {
                Map<String, Object> map = iterator.next();
                buff_xml.append("<Insurant>");
                buff_xml.append("<IDType>" + map.get("IDType") + "</IDType>");
                buff_xml.append("<ID>" + map.get("IDNo") + "</ID>");
                buff_xml.append("<Name>" + map.get("InsuredName") + "</Name>");
                buff_xml.append("<Sex>" + map.get("sex") + "</Sex>");
                buff_xml.append("<Birthday>" + map.get("Birthday") + "</Birthday>");
                buff_xml.append("<CellPhoneNumber></CellPhoneNumber>");
                buff_xml.append("<Address></Address>");
                buff_xml.append("<Telephone></Telephone>");
                buff_xml.append("<Email></Email>");
                buff_xml.append(
                        "<InsurantApplicantRelation>" + map.get("RelationToInsured") + "</InsurantApplicantRelation>");
                buff_xml.append("<BenefitInfo>");
                buff_xml.append("<IsLegal>" + map.get("IsLegal") + "</IsLegal>");
                buff_xml.append("<BeneficiaryList>");

                // =====================================收益人信息=======================================
                sql = "SELECT * FROM picc_teambenefit WHERE batchno = ? AND ipersonid = ?";
                List<Map<String, Object>> picc_teambenefit = jdbcTemplate.queryForList(sql,
                        new Object[]{BatchNo, map.get("id")});

                if (picc_teambenefit == null || picc_teambenefit.size() == 0) {
                    buff_xml.append("<Beneficiary>");
                    buff_xml.append("<Index></Index>");
                    buff_xml.append("<IDType></IDType>");
                    buff_xml.append("<ID></ID>");
                    buff_xml.append("<Name></Name>");
                    buff_xml.append("<Sex></Sex>");
                    buff_xml.append("<Birthday></Birthday>");
                    buff_xml.append("<CellPhoneNumber></CellPhoneNumber>");
                    buff_xml.append("<Address></Address>");
                    buff_xml.append("<Telephone></Telephone>");
                    buff_xml.append("<Email></Email>");
                    buff_xml.append("<Relation></Relation>");
                    buff_xml.append("<Scale></Scale>");
                    buff_xml.append("</Beneficiary>");
                } else {
                    for (Iterator<Map<String, Object>> iterator2 = picc_teambenefit.iterator(); iterator2.hasNext(); ) {
                        Map<String, Object> map_teambenefit = (Map<String, Object>) iterator2.next();
                        buff_xml.append("<Beneficiary>");
                        buff_xml.append("<Index>" + map_teambenefit.get("Index1") + "</Index>");
                        buff_xml.append("<IDType>" + map_teambenefit.get("IDType") + "</IDType>");
                        buff_xml.append("<ID>" + map_teambenefit.get("IDNo") + "</ID>");
                        buff_xml.append("<Name>" + map_teambenefit.get("name") + "</Name>");
                        buff_xml.append("<Sex>" + map_teambenefit.get("sex") + "</Sex>");
                        buff_xml.append("<Birthday>" + map_teambenefit.get("Birthday") + "</Birthday>");
                        buff_xml.append("<CellPhoneNumber></CellPhoneNumber>");
                        buff_xml.append("<Address></Address>");
                        buff_xml.append("<Telephone></Telephone>");
                        buff_xml.append("<Email></Email>");
                        buff_xml.append("<Relation>00</Relation>");
                        buff_xml.append("<Scale>" + map_teambenefit.get("Scale") + "</Scale>");
                        buff_xml.append("</Beneficiary>");
                    }
                }

                // =====================================收益人信息=======================================

                buff_xml.append("</BeneficiaryList>");
                buff_xml.append("</BenefitInfo>");
                buff_xml.append("</Insurant>");
            }

            buff_xml.append("</InsurantList>");

            buff_xml.append("</InsuredInfo>");
            // =====================================投保人信息=======================================

            buff_xml.append("</ApplyInfo>");

            buff_xml.append("<Payment>");
            buff_xml.append("<PaymentId></PaymentId>");
            buff_xml.append("<BankSerial></BankSerial>");
            buff_xml.append("<PayBankCode></PayBankCode>");
            buff_xml.append("<PayBankAcount></PayBankAcount>");
            buff_xml.append("<PayBankUserId></PayBankUserId>");
            buff_xml.append("<ReciveBankCode></ReciveBankCode>");
            buff_xml.append("<ReciveBankAcount></ReciveBankAcount>");
            buff_xml.append("<ReciveBankUserId></ReciveBankUserId>");
            buff_xml.append("<ReciveBankUserName></ReciveBankUserName>");
            buff_xml.append("<PayType></PayType>");
            buff_xml.append("<PayTime>" + Time + "</PayTime>");
            buff_xml.append("<PayMoney>" + map1.get("realprem") + "</PayMoney>");
            buff_xml.append("<AccountDate></AccountDate>");
            buff_xml.append("</Payment>");

            buff_xml.append("<OrderInfo>");
            buff_xml.append("<OrderId>" + BatchNo + "</OrderId>");
            buff_xml.append("</OrderInfo>");

            buff_xml.append("<Flight>");
            buff_xml.append("<FltNO>" + order_res.get(0).get("Remark") + "</FltNO>");
            buff_xml.append("</Flight>");

            buff_xml.append("<OtherInfo>");
            buff_xml.append("<InsureNO>1</InsureNO>");
            buff_xml.append("<Plat>ZFB</Plat>");
            buff_xml.append("<ConIndProxyNO>hengsheng001</ConIndProxyNO>");

            buff_xml.append("</OtherInfo>");

            buff_xml.append("</IssueItem>");
            buff_xml.append("</IssueList>");
            buff_xml.append("</Request>");
            buff_xml.append("</Package>");
            buff_xml.append("</PackageList>");

            // 发送数据至保险网关
            logger.info("发送数据：" + buff_xml.toString() + "\n");

            if (true)
                return "";

            jdbcTemplate.update("UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?", new Object[]{5, BatchNo});

            List<Map<String, Object>> send_pa = jdbcTemplate.queryForList(
                    "SELECT interface, interkey FROM insproject WHERE id  = ?", new Object[]{projectid});
            if (send_pa == null || send_pa.size() == 0) {
                return "方案错误";
            }

            final String interface_url = (String) send_pa.get(0).get("interface");
            final String interkey = (String) send_pa.get(0).get("interkey");

            String result = CommsBx.sendInfo2BxGetWay(jdbcTemplate, interkey, buff_xml, interface_url, BatchNo);

            if (result == null || result.indexOf("err") != -1) {
                return result;
            }

            logger.info("得到返回结果：          " + result + "\n");
            logger.info(
                    "=========================================解析返回xml============================================\n");

            String bx_url = null;
            String FailReason = null;
            String PolicyNo = null;

            SAXReader saxReader = new SAXReader();
            try {
                Document document = saxReader.read(new ByteArrayInputStream(result.getBytes("UTF-8")));
                Element info = document.getRootElement();
                for (Iterator i = info.elementIterator(); i.hasNext(); ) {
                    Element info1 = (Element) i.next();
                    for (Iterator j = info1.elementIterator(); j.hasNext(); ) {
                        Element info2 = (Element) j.next();
                        for (Iterator k = info2.elementIterator(); k.hasNext(); ) {
                            Element info3 = (Element) k.next();
                            System.out.println(info3.getName() + ":" + info3.getText());
                            for (Iterator l = info3.elementIterator(); l.hasNext(); ) {
                                Element info4 = (Element) l.next();
                                System.out.println(info4.getName() + ":" + info4.getText());
                                for (Iterator m = info4.elementIterator(); m.hasNext(); ) {
                                    Element node = (Element) m.next();

                                    System.out.println(node.getName() + ":" + node.getText());

                                    if ("FailReason".equals(node.getName())) {
                                        FailReason = node.getText();
                                    }

                                    if ("PolicyNo".equals(node.getName())) {
                                        PolicyNo = node.getText();
                                    }
                                }
                            }
                        }
                    }
                    System.out.println();
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            logger.info("=========================================解析完成 ============================================\n");

            if (result.indexOf("<IsSuccess>1</IsSuccess>") == -1) {
                // jdbcTemplate
                // .update("INSERT INTO orderlog
                // (batchno,front,behind,note,dealtime,companyid) VALUES
                // (?,5,6,'出单失败',now(),?)",
                // new Object[] { BatchNo, companyid });
                return "出单失败，失败原因： " + FailReason;
            }
            // jdbcTemplate.update(
            // "UPDATE picc_teamorder SET downurl = ? WHERE batchno = ?",
            // new Object[] { bx_url, BatchNo });
            // jdbcTemplate.update(
            // "UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?",
            // new Object[] { 7, BatchNo });
            // jdbcTemplate
            // .update("INSERT INTO orderlog
            // (batchno,front,behind,note,dealtime,companyid) VALUES
            // (?,5,7,'出单成功',now(),?,?)",
            // new Object[] { BatchNo, companyid, usr });
            // jdbcTemplate.update("getServerJYXML picc_teamorder SET grpcontno
            // = ? where batchno = ?",
            // new Object[] { PolicyNo, BatchNo });
            String sin = JM.MD5("hengsheng#2015" + PolicyNo);
            String url = "http://app.lianlife.com/BS/Policy?channelNum=hengsheng&policyNo=" + PolicyNo + "&sign=" + sin;

            jdbcTemplate.update("UPDATE picc_teamorder SET downurl = ? where batchno = ?",
                    new Object[]{url, BatchNo});

            return "0000";
        } else if (companyid == 2) {
            return "0000";
        } else if (companyid == 7) {
            return ZJbx.zjbx(jdbcTemplate, BatchNo, companyid, projectid, medid);
        } else if (companyid == 8) {
            return DBbx.dbbx(jdbcTemplate, BatchNo, companyid, projectid, medid);
        } else if (companyid == 9) {
            return YTBX.ytbx(jdbcTemplate, BatchNo, companyid, projectid, medid);
        } else if (companyid == 10) {
            return RMBX.rmbx(jdbcTemplate, BatchNo, companyid, projectid, medid);
        } else if (companyid == 11) {
            return RBC.rbc(jdbcTemplate, BatchNo, companyid, projectid, medid);
        } else if (companyid == 12) {
            return TPXbx.tpy(jdbcTemplate, BatchNo, companyid, projectid, medid);
        } else if (companyid == 13) {
            try {
                return HNBX.hnbx(jdbcTemplate, BatchNo, companyid, projectid, medid);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return "companyid err";
        }
    }

    public static String pgbw(String BatchNo, String pg, String subNo) {

        context = new ClassPathXmlApplicationContext("com/gopay/spring_c3p0.xml");
        final JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");

        final StringBuffer buff_xml = new StringBuffer();
        buff_xml.append("<?xml version=\"1.0\" encoding=\"gb2312\" standalone=\"yes\"?>");
        // List<Map<String, Object>> r1 = jdbcTemplate
        // .queryForList("SELECT GrpContNo FROM picc_teamorder WHERE BatchNo =
        // ?", new Object[] { BatchNo });

        List<Map<String, Object>> picc_suborder_res = jdbcTemplate.queryForList(
                "SELECT * FROM picc_suborder WHERE BatchNo  = ? and SubBatchNo = ?",
                new Object[]{BatchNo, subNo});


        buff_xml.append("<ModifyApply>");
        buff_xml.append("<RequestHeader>");
        String uuid = "BYW" + System.currentTimeMillis();
        buff_xml.append("<UUID>" + uuid + "</UUID>");
        List<Map<String, Object>> m = jdbcTemplate
                .queryForList("SELECT agentcode FROM toinscompany WHERE insid = 11 AND kind=1");

        buff_xml.append("<PlateformCode>" + m.get(0).get("agentcode") + "</PlateformCode>");

        buff_xml.append("<Md5Value>" + MD5.md5(uuid + picc_suborder_res.get(0).get("GrpContNo")) + "</Md5Value>");

        buff_xml.append("<ApplyType>H0002</ApplyType>");
        buff_xml.append("</RequestHeader>");

        buff_xml.append("<ModifyInfos>");

        List<Map<String, Object>> picc_subperson_res = null;
        if ("00".equals(pg)) {
            picc_subperson_res = jdbcTemplate.queryForList(
                    "SELECT * FROM picc_subperson WHERE BatchNo  = ? and RelationToInsured='00'",
                    new Object[]{BatchNo});

        } else {
            picc_subperson_res = jdbcTemplate.queryForList(
                    "SELECT * FROM picc_subperson WHERE BatchNo  = ? and RelationToInsured <> '00'",
                    new Object[]{BatchNo});
        }

        buff_xml.append("<ModifyInfo>");
        buff_xml.append("<EndorseType>P01</EndorseType>");
        buff_xml.append("<ApplyNO>" + BatchNo + "</ApplyNO>");
        buff_xml.append("<PolicyNo>" + picc_suborder_res.get(0).get("GrpContNo") + "</PolicyNo>");
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        buff_xml.append("<EndorseDate>" + s.format(d).substring(0, 19) + "</EndorseDate>");
        buff_xml.append("<PlanCode>EAC0000001</PlanCode>");
        buff_xml.append("<StartDate>" + picc_suborder_res.get(0).get("CValiDate").toString().substring(0, 10)
                + "</StartDate>");
        buff_xml.append(
                "<EndDate>" + picc_suborder_res.get(0).get("CInValiDate").toString().substring(0, 10) + "</EndDate>");
        // buff_xml.append("<Fee>"+picc_subperson_res.get(0).get("afterfee")+"</Fee>");
        buff_xml.append("<NatureInfos>");

        for (Map<String, Object> map : picc_subperson_res) {

            buff_xml.append("<NatureInfo>");
            String f = (String) map.get("RelationToInsured");
            if ("00".equals(f)) {
                f = "2";
            } else {
                f = "1";
            }
            buff_xml.append("<InsuredFlag>" + f + "</InsuredFlag>");
            buff_xml.append("<InsuredName>" + map.get("InsuredName") + "</InsuredName>");

            String idType = map.get("IDType").toString();
            if ("0".equals(idType)) {
                idType = "01";
            } else if ("5".equals(idType)) {
                idType = "02";
            } else if ("1".equals(idType)) {
                idType = "03";
            } else if ("2".equals(idType)) {
                idType = "04";
            } else if ("3".equals(idType)) {
                idType = "05";
            } else if ("71".equals(idType)) {
                idType = "31";
            } else if ("95".equals(idType)) {
                idType = "37";
            } else {
                idType = "99";
            }

            buff_xml.append("<IdentifiedType>" + idType + "</IdentifiedType>");
            buff_xml.append("<IdentifyNumber>" + map.get("IDNo") + "</IdentifyNumber>");
            buff_xml.append("<InsuredEName></InsuredEName>");
            buff_xml.append("<Sex>" + (Integer.parseInt(map.get("sex") + "") + 1) + "</Sex>");
            buff_xml.append("<Birthday>" + map.get("Birthday") + "</Birthday>");
            buff_xml.append("<EndorseFlag>" + map.get("subflag") + "</EndorseFlag>");
            buff_xml.append("</NatureInfo>");
        }

        buff_xml.append("</NatureInfos></ModifyInfo></ModifyInfos></ModifyApply>");
        System.out.println(buff_xml);
//		/**
//		 * 标准系统对接起保前批改请求，调用modifyService方法，interfaceNo值为固定值“001004”
//		 */

        EcooperationWebServiceService ewss = new EcooperationWebServiceService();
        EcooperationWebService ews = ewss.getPort(EcooperationWebService.class);
        String res = ews.modifyService("001004", buff_xml.toString());

        System.out.println(res);
        return "0000";
    }

    // 2015071016354239142

    // =2016020319320381917&projectid=16&companyid=7&medid=1
    public static void main(String[] args) throws IOException {

        // String s = sendOrder2GetwaySync("BYW2016071308352899505", 34, 8, 1);
        // System.out.println(s);
        // CommServerWebService ems = new CommServerWebService();
        // CommServerWebServicePortType em
        // =<GRPCONTDATA><MSGHEAD><ITEM><BatchNo>BYW2017040712254978245</BatchNo><SendDate>2017-04-10</SendDate><SendTime>09:56:50</SendTime><SendOperator>HKXS</SendOperator><MsgType>WX_GRP06</MsgType></ITEM></MSGHEAD><GRPCONTLIST><ITEM><PrtNo>1</PrtNo><ManageCom>86320000</ManageCom><SaleChnl>03</SaleChnl><HandlerDate>2017-04-10</HandlerDate><AgentCom>PI3200001507</AgentCom><AgentCode>2232131503</AgentCode><FirstTrialOperator>gzh</FirstTrialOperator><MarketType>1</MarketType><ReceiveDate>2017-04-10</ReceiveDate><CoInsuranceFlag>1</CoInsuranceFlag><PayMode>1</PayMode><BankAccNo></BankAccNo><AccName></AccName><PayIntv>0</PayIntv><CValiDate>2017-04-10</CValiDate><CInValiDate>2018-04-09</CInValiDate><PeoplesInsu>2</PeoplesInsu><Remark></Remark><EjbAgent>86320000</EjbAgent></ITEM></GRPCONTLIST><GRPAPPNTLIST><ITEM><GrpNo>1</GrpNo><GrpName>测试22</GrpName><Phone>88888888</Phone><OrgancomCode>测试22-11</OrgancomCode><UnifiedSocialCreditNo></UnifiedSocialCreditNo><GrpAddress>测试22</GrpAddress><GrpZipCode></GrpZipCode><TaxNo></TaxNo><LegalPersonName>测试1</LegalPersonName><LegalPersonIDNo>112345678</LegalPersonIDNo><LinkMan1>测试1</LinkMan1><Phone1>88888888</Phone1><Fax1></Fax1><E_Mail1></E_Mail1><BusinessType></BusinessType><BusinessBigType></BusinessBigType><GrpNature></GrpNature><Peoples></Peoples><AppntOnWorkPeoples></AppntOnWorkPeoples><AppntOffWorkPeoples></AppntOffWorkPeoples><AppntOtherPeoples></AppntOtherPeoples><Peoples3>2</Peoples3><OnWorkPeoples></OnWorkPeoples><OffWorkPeoples></OffWorkPeoples><OtherPeoples></OtherPeoples><RelaPeoples></RelaPeoples><RelaMatePeoples></RelaMatePeoples><RelaYoungPeoples></RelaYoungPeoples></ITEM></GRPAPPNTLIST><GRPINSULIST><ITEM><InsuredID>2</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>27</RelationToInsured><MainInsuredName>测试22</MainInsuredName><InsuredName>测试21</InsuredName><Sex>0</Sex><Birthday>1980-08-23</Birthday><IDType>0</IDType><IDNo>320111198008232416</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM><ITEM><InsuredID>3</InsuredID><ContID></ContID><Retire></Retire><RelationToInsured>27</RelationToInsured><MainInsuredName>测试22</MainInsuredName><InsuredName>测试22</InsuredName><Sex>0</Sex><Birthday>1990-09-12</Birthday><IDType>0</IDType><IDNo>320122199009122412</IDNo><ContPlanCode>A</ContPlanCode><OccupationType>1</OccupationType><OccupationCode></OccupationCode><BankCode></BankCode><BankAccNo></BankAccNo><AccName></AccName><Phone></Phone></ITEM></GRPINSULIST><GRPWRAPLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><MainRiskCode>560801</MainRiskCode><Amnt>220000</Amnt><Prem>451.0</Prem><Mult></Mult><Copys></Copys><PayEndYearFlag></PayEndYearFlag><PayEndYear></PayEndYear><InsuYearFlag>M</InsuYearFlag><InsuYear>12</InsuYear></ITEM></GRPWRAPLIST><WRAPPARAMLIST><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>560801</RiskCode><DutyCode>684001</DutyCode><FacValAmnt>100000</FacValAmnt><Prem>187.0</Prem><FacValAmntType>0</FacValAmntType></ITEM><ITEM><RiskWrapCode>ZZJ078</RiskWrapCode><RiskCode>1607</RiskCode><DutyCode>614002</DutyCode><FacValAmnt>10000</FacValAmnt><Prem>38.5</Prem><FacValAmntType>0</FacValAmntType></ITEM></WRAPPARAMLIST></GRPCONTDATA>
        // ems.getPort(CommServerWebServicePortType.class);
        //
        // String es = MD5.md5("instony1234"+s);
        // String s1 = em.generatePolicyService(s,es);
        // System.out.println(s1+"======");

        // 退保 <policyno>91000000000000063897</policyno>
        // em.withdrawPolicyService(in0, in1);

        // System.out.println(s1);

        // final String BatchNo, int projectid, int companyid, int medid
        // BYW2016090711232121836&projectid=30&companyid=7&medid=6
        // 人保财
        //45/ 47 49
        //BYW2017090115595886387&projectid=52&companyid=12&medid=1
        //        String s = sendOrder2GetwaySync("BYW2017112220125354301", 39, 10, 1);
        //
        //		 System.out.println(s);
        //		// 人保健
        //String s = sendOrder2GetwaySync("BYW2017061512424183331", 39, 10, 1);
        //System.out.println(s);
        String s = sendOrder2GetwaySync("BYW2018013108341268915", 67, 13, 1);
        System.out.println(s);

//		pgbw("BYW2017051814593726952", "00", "SUB_1495433276233");

        // <DownloadUrl><![CDATA[http://partnertest.mypicc.com.cn/ecooperation/policydownload/downloadurl.do?platfromcodes=CPI000178&policyNo=F64E36CF95F85A45941F66EACE6E7312B6B9C8EC1A9819646B68BEB2BA4DDB1D&insuredID=77DA6F0032A9F85368952281FF2A41B3C71ECB49B33DA0BBC94D794FA4029E0E&flag=N]]></DownloadUrl>

    }
}
