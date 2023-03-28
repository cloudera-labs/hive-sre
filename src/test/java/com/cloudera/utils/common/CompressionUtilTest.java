/*
 * Copyright (c) 2022. Cloudera, Inc. All Rights Reserved
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.cloudera.utils.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class CompressionUtilTest {
    private CompressionUtil cu = CompressionUtil.getInstance();
    private ObjectMapper om = new ObjectMapper();

    @Test
    public void testGet_001() throws JsonProcessingException {
        // dag plan
        String byteStrA = "01000006BCF0117B226461674E616D65223A2253454C4543545C6E2020202042415443485F49440E00F0132C20534F555243452E2E2E5441524745545F484F5354202853746167652D3129222C4500F11B496E666F223A227B5C22636F6E746578745C223A5C22486976655C222C5C226465736372697074696F6E1900036D00006000076E00000F00046F0001660003120007780003120080434F554E542831293F0007020083415454454D5054532600F30C524F554E4428415647284C4554414E4359292C203329204156475F1100032900354D494E2300074F003C4D494E29002F415829000227415829004146524F4D5900F302202050494E475C5C6E47524F55502042591600045F0109EC000AE70022227D580112434E01F33A223A7B2263616C6C65724964223A22686976655F32303232313032313037353631365F65626264313061652D343538352D343862372D393161342D353265313539366466623263222C460030547970F401F400484956455F51554552595F4944222CBC01031A0000820006B8010F23021002B40101C50108B3010111000FB2010D0125000FB101100128000FB001100228000FAF010F02AE0101F60201AD0106AC010114000FAB010F81227D2C2276657273230111320C00907469636573223A5B7B1900337465786203F01C4D61702031222C2270726F636573736F72436C617373223A226F72672E6170616368652E6861646F6F702EE101F4052E716C2E657865632E74657A2E4D617054657A503A00C0222C226F75744564676549646A00F00322393939323531323032225D2C22616464696B0370616C496E7075742100327B226EE7034070696E67EA010E7B00006700B06D61707265647563652E693A00312E4D524200FF054C6567616379222C22696E697469616C697A6572B80013000204FB0453706C697447656E657261746F72227D5D7D2C1A01115274003F7220321E01240240000B21012F696E20010F3C4F75742101426F75745F50000290000F2A010E116F4400322E4D524D0000E000635D2C226564670402000A0001B60306A401009D004F707574561E0201024F00091B00088F00C3646174614D6F76656D656E74C603205343F80480525F474154484552B60393617461536F757263652200805045525349535445E403A37363686564756C696E671D00B253455155454E5449414C22B700023B000F9D0200002202F20172756E74696D652E6C6962726172792EAF00C12E4F726465726564506172747B024365644B561001020C017044657374696E6197020F5A00140123010459005047726F7570550090496E707574227D5D7D";
        byte[] val = stringToByteA(byteStrA);
        ObjectNode dagPlan = cu.getValue(val, ObjectNode.class);
        System.out.println(om.writeValueAsString(dagPlan));
    }

    @Test
    public void testGet_002() throws JsonProcessingException {
        String explainba = "01000001FCF0337B2263626F496E666F223A22506C616E206F7074696D697A65642062792043424F2E222C22535441474520444550454E44454E43494553223A7B2253746167652D300B0051524F4F54202600A4223A2254525545227D7D37004C504C414E3000E04665746368204F70657261746F724200F00E6C696D69743A223A222D31222C2250726F636573736F7220547265653A2100905461626C655363616E0D0050616C6961732E00D074657374222C22636F6C756D6E1200E05B2261225D2C22646174616261733B00F00222707269765F64737472656576222C22744900073C0061697354656D705F000016008466616C7365222C229D0020496416004054535F3068006168696C64726584006953656C656374C5009165787072657373696F8B00E022612028747970653A20696E74293E0001A60071457870724D6170D500505F636F6C302A00B1227D2C226F7574707574432500424E616D65CF000223001C5D91004053454C5F2A01089200804C69737453696E6B22010ABF00F0074C4953545F53494E4B5F33227D7D7D7D7D7D7D7D7D7D";
        byte[] expBA = stringToByteA(explainba);
        ObjectNode explainPlan = cu.getValue(expBA, ObjectNode.class);
        System.out.println(om.writeValueAsString(explainPlan));
    }

    protected byte[] stringToByteA(String strByteA) {
        byte[] val = new byte[strByteA.length()/2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(strByteA.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }
}