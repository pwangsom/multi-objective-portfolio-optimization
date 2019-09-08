package com.amontep.portfolio.stock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stock {
	
	private Double returnProfit;
	private Double risk;
	private Double companyPerformance;
	
	@Getter
	private Double expectedReturns[] = { 0.595020488144665, 0.539298734350644, 0.649819521476015, 0.550553552777500,
            0.748752198094304, 0.638370347853793, 0.581244031909298, 0.796757683789770, 0.678623841253730,
            0.617071597168883, 0.854761904761905, 0.726670110192838, 0.758213083213084, 0.827890949630081,
            0.708570217661127, 0.683819803385021, 0.611593898525717, 0.824565531087271, 0.614357205688475,
            0.723651810458407, 0.640692640692641, 0.590900281460054 };
	
	@Getter
	private Double covarianceMatrix[][] = {
            {0.0337770663280722,0.0263874658166789,0.0365115463631896,0.0237148235650089,0.0442599767825083,0.0296249796607968,0.0227365484945410,0.0435360874412588,0.0284929675019290,0.0213985418877040,0.0424019375685069,0.0268616953344749,0.0216093106221949,0.0177658502496875,0.00708808550657857,-0.00609040886858701,-0.0104668932615510,-0.00410738221317459,-0.0235899107304179,-0.0288232102596069,-0.0434120227399024,-0.0442061356695758},
            {0.0263874658166789,0.0217232005033316,0.0286289237945177,0.0203944932785111,0.0340741182303698,0.0248373606023603,0.0202020560589946,0.0344787222456829,0.0247173707983783,0.0198054668588898,0.0347408237524269,0.0243286050625256,0.0214044474855984,0.0195410827948028,0.0110734044548828,0.00101972687933448,-0.00301787536535328,0.00371760304279758,-0.0136323120850852,-0.0169162181876423,-0.0301015355975632,-0.0313531315312820},
            {0.0365115463631896,0.0286289237945177,0.0394773914472970,0.0258118803663889,0.0477953620687213,0.0321840155470965,0.0248085667401937,0.0471049898171985,0.0310327977543945,0.0234236290925317,0.0459879723381868,0.0293533183193040,0.0237871486522629,0.0197400537724608,0.00818527873662963,-0.00603831960920526,-0.0108278216959140,-0.00378612383073515,-0.0250478932184388,-0.0306291030780210,-0.0465666923872374,-0.0474841945185000},
            {0.0237148235650089,0.0203944932785111,0.0258118803663889,0.0197985557484784,0.0302282055896079,0.0236535711386590,0.0200768329651000,0.0313535768943792,0.0241482330987406,0.0202331273786722,0.0324918379803403,0.0244981224085481,0.0228046273074118,0.0220351426007387,0.0143360877974880,0.00550923736100997,0.00139443394802044,0.00884299688643444,-0.00842554074152137,-0.0107350043586850,-0.0240225094853059,-0.0256564715540781},
            {0.0442599767825083,0.0340741182303698,0.0477953620687213,0.0302282055896079,0.0582246215568967,0.0380513197438440,0.0286876630772809,0.0568357069763352,0.0362219576663645,0.0266413041121105,0.0548288889704928,0.0336839820330150,0.0262697561174656,0.0207206347712713,0.00679013755625738,-0.0105804809491807,-0.0160346234148391,-0.00850051058566002,-0.0330643514725182,-0.0402828929630659,-0.0586013442373090,-0.0593608581957290},
            {0.0296249796607968,0.0248373606023603,0.0321840155470965,0.0236535711386590,0.0380513197438440,0.0285706451040924,0.0236697120210252,0.0388980511130094,0.0287460544748450,0.0234881841219669,0.0396572635017627,0.0286693648166948,0.0258667615570202,0.0242397149271525,0.0146859202569627,0.00350365010776995,-0.00127975909223887,0.00699998806970653,-0.0133416210535114,-0.0166991095625464,-0.0322383496766782,-0.0339040036563697},
            {0.0227365484945410,0.0202020560589946,0.0248085667401937,0.0200768329651000,0.0286876630772809,0.0236697120210252,0.0206805358592238,0.0303332767516736,0.0245934533879363,0.0212136574130301,0.0320967686627185,0.0254512604341071,0.0245275882188037,0.0244690876122212,0.0170269043052301,0.00872977445576012,0.00442283857087796,0.0126063081076814,-0.00519856631708611,-0.00692964048479739,-0.0207571641095638,-0.0227099914990499},
            {0.0435360874412588,0.0344787222456829,0.0471049898171985,0.0313535768943792,0.0568357069763352,0.0388980511130094,0.0303332767516736,0.0563117366366314,0.0377607690900344,0.0288814786804721,0.0553337665032341,0.0360307630544495,0.0297561322083504,0.0252802397226174,0.0114621224200593,-0.00542618765609537,-0.0113278846894968,-0.00238743827559453,-0.0283961462410450,-0.0348044912822015,-0.0543549834993683,-0.0556417240129641},
            {0.0284929675019290,0.0247173707983783,0.0310327977543945,0.0241482330987406,0.0362219576663645,0.0287460544748450,0.0245934533879363,0.0377607690900344,0.0294883440033993,0.0249073280582616,0.0393497667067511,0.0300806115626139,0.0282760404153718,0.0275747150179677,0.0183038721437976,0.00775207167190270,0.00268900084841751,0.0119812646662481,-0.00917780110635827,-0.0117939774114826,-0.0281153378355562,-0.0302050267267845},
            {0.0213985418877040,0.0198054668588898,0.0234236290925317,0.0202331273786722,0.0266413041121105,0.0234881841219669,0.0212136574130301,0.0288814786804721,0.0249073280582616,0.0221849871225567,0.0313620286592347,0.0263542296774583,0.0263439429289300,0.0271237822736311,0.0200511910912684,0.0124552960185591,0.00796005671834358,0.0169378971942872,-0.00134477181843508,-0.00237860579611359,-0.0167395305963624,-0.0190565823041889},
            {0.0424019375685069,0.0347408237524269,0.0459879723381868,0.0324918379803403,0.0548288889704928,0.0396572635017627,0.0320967686627185,0.0553337665032341,0.0393497667067511,0.0313620286592347,0.0555828879170714,0.0385920674185979,0.0337158514199330,0.0305500066990750,0.0169612473694106,0.000768055271160939,-0.00562727852268659,0.00493056435824855,-0.0226296835094739,-0.0280280154707225,-0.0489444984342941,-0.0508598806676423},
            {0.0268616953344749,0.0243286050625256,0.0293533183193040,0.0244981224085481,0.0336839820330150,0.0286693648166948,0.0254512604341071,0.0360307630544495,0.0300806115626139,0.0263542296774583,0.0385920674185979,0.0314661123045362,0.0308742600502676,0.0312903120991772,0.0224571419176328,0.0127768902913596,0.00743133862026445,0.0178418313043459,-0.00408143886117199,-0.00578085817710307,-0.0228984311486629,-0.0254833015112217},
            {0.0216093106221949,0.0214044474855984,0.0237871486522629,0.0228046273074118,0.0262697561174656,0.0258667615570202,0.0245275882188037,0.0297561322083504,0.0282760404153718,0.0263439429289300,0.0337158514199330,0.0308742600502676,0.0323952328780371,0.0346738053501339,0.0274225177008109,0.0201517742666812,0.0148296686140044,0.0261641611772245,0.00499623108822749,0.00501871999706434,-0.0119095337349304,-0.0151142261825548},
            {0.0177658502496875,0.0195410827948028,0.0197400537724608,0.0220351426007387,0.0207206347712713,0.0242397149271525,0.0244690876122212,0.0252802397226174,0.0275747150179677,0.0271237822736311,0.0305500066990750,0.0312903120991772,0.0346738053501339,0.0386278169613927,0.0325324415538550,0.0271406623740492,0.0216820323490828,0.0341509788624742,0.0130003873407896,0.0145117415569148,-0.00281900064508743,-0.00667338779098604},
            {0.00708808550657857,0.0110734044548828,0.00818527873662963,0.0143360877974880,0.00679013755625738,0.0146859202569627,0.0170269043052301,0.0114621224200593,0.0183038721437976,0.0200511910912684,0.0169612473694106,0.0224571419176328,0.0274225177008109,0.0325324415538550,0.0299003988833641,0.0287846073147729,0.0247812618052769,0.0350825323923656,0.0203225864947849,0.0234669308716702,0.0107161065417096,0.00708851161155350},
            {-0.00609040886858701,0.00101972687933448,-0.00603831960920526,0.00550923736100997,-0.0105804809491807,0.00350365010776995,0.00872977445576012,-0.00542618765609537,0.00775207167190270,0.0124552960185591,0.000768055271160939,0.0127768902913596,0.0201517742666812,0.0271406623740492,0.0287846073147729,0.0331207992065837,0.0306935964301697,0.0389791180703956,0.0313401693954855,0.0368330876283423,0.0289454047218023,0.0253099411912841},
            {-0.0104668932615510,-0.00301787536535328,-0.0108278216959140,0.00139443394802044,-0.0160346234148391,-0.00127975909223887,0.00442283857087796,-0.0113278846894968,0.00268900084841751,0.00796005671834358,-0.00562727852268659,0.00743133862026445,0.0148296686140044,0.0216820323490828,0.0247812618052769,0.0306935964301697,0.0291791772107571,0.0356546540607370,0.0317306129096310,0.0374546271503232,0.0324504418312396,0.0292674608600510},
            {-0.00410738221317459,0.00371760304279758,-0.00378612383073515,0.00884299688643444,-0.00850051058566002,0.00699998806970653,0.0126063081076814,-0.00238743827559453,0.0119812646662481,0.0169378971942872,0.00493056435824855,0.0178418313043459,0.0261641611772245,0.0341509788624742,0.0350825323923656,0.0389791180703956,0.0356546540607370,0.0461717279468011,0.0351747140064807,0.0412364789823577,0.0304955336380951,0.0261011013277021},
            {-0.0235899107304179,-0.0136323120850852,-0.0250478932184388,-0.00842554074152137,-0.0330643514725182,-0.0133416210535114,-0.00519856631708611,-0.0283961462410450,-0.00917780110635827,-0.00134477181843508,-0.0226296835094739,-0.00408143886117199,0.00499623108822749,0.0130003873407896,0.0203225864947849,0.0313401693954855,0.0317306129096310,0.0351747140064807,0.0395189843075718,0.0470463165940137,0.0481891058770933,0.0454755940704472},
            {-0.0288232102596069,-0.0169162181876423,-0.0306291030780210,-0.0107350043586850,-0.0402828929630659,-0.0166991095625464,-0.00692964048479739,-0.0348044912822015,-0.0117939774114826,-0.00237860579611359,-0.0280280154707225,-0.00578085817710307,0.00501871999706434,0.0145117415569148,0.0234669308716702,0.0368330876283423,0.0374546271503232,0.0412364789823577,0.0470463165940137,0.0560352681472781,0.0579172174374573,0.0547767958704283},
            {-0.0434120227399024,-0.0301015355975632,-0.0465666923872374,-0.0240225094853059,-0.0586013442373090,-0.0322383496766782,-0.0207571641095638,-0.0543549834993683,-0.0281153378355562,-0.0167395305963624,-0.0489444984342941,-0.0228984311486629,-0.0119095337349304,-0.00281900064508743,0.0107161065417096,0.0289454047218023,0.0324504418312396,0.0304955336380951,0.0481891058770933,0.0579172174374573,0.0697364804507661,0.0682636576532844},
            {-0.0442061356695758,-0.0313531315312820,-0.0474841945185000,-0.0256564715540781,-0.0593608581957290,-0.0339040036563697,-0.0227099914990499,-0.0556417240129641,-0.0302050267267845,-0.0190565823041889,-0.0508598806676423,-0.0254833015112217,-0.0151142261825548,-0.00667338779098604,0.00708851161155350,0.0253099411912841,0.0292674608600510,0.0261011013277021,0.0454755940704472,0.0547767958704283,0.0682636576532844,0.0672949181349059},
    };

}
