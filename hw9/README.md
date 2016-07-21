## W251 HW9 - Spark Streaming

Chris Murray


### To execute

	cd hw9
	./run.sh


### To adjust parameters

	vi run.sh


### Interesting design decision

Getting a list of popular hashtags was easy.  Keeping track of authors associated with
the hashtags was a little more difficult.  But keeping track of authors plus mentions 
proved challenging for me, until I finally figured out that the value for my key:value 
pair could be an object!  Once I realized that it was rather simple.


### Example output:

	Popular topics in last 1800 seconds (835 total):

	Hashtag:  #MTVHottest (115 tweets)
	Authors:  @melow324,@yasminsantossz1,@lmfor631,@lmfor432,@lmfor685,@lmfor678,@TropaTTT75,@lmfor641,@BrownieDinah,@drewzfucker,@teamahfod26,@lmfor508,@lmfor578,@lmfor630,@lmfor577,@lmfor542,@pookkyy89,@mtvvotosCp_266,@lmfor691,@divxharry,@guigui_cast,@lmfor598,@lmfor593,@lmfor617,@lmfor801,@lmfor462,@lmfor530,@Eduardaa_almeid,@lmfor767,@lmfor748,@lmfor755,@lmfor828,@lmfor787,@lmfor830,@nortiz_6,@Nortiz88,@OrtizLill,@magconfhelp,@lmfor458,@lmfor543,@lmfor648,@lmfor417,@dayumjacobz,@lmfor683,@lmfor634,@teamahfod42,@teamahfod61,@mtvvotosCp_162,@teamahfod62,@BieberVotos3,@teamahfod57,@pookkyy102,@lmfor460,@siete6666666,@lmfor599,@lmfor526,@ruth_adriana04,@lmfor499,@lmfor503,@luarcamren,@Idriannnaaa,@votebieberszws,@mtvvotosCp_286,@lmfor720,@lmfor465,@alineaiasca,@Pytter_Solpha,@sorryajb4,@5sosaf69,@lmfor551,@lmfor546,@lmfor541,@cutebiebsx,@matthewaffles_,@voterepublic,@LariDrew_94,@ViihBelieber11,@lmfor700,@lmfor702,@lovatics_chatos,@KeixjskdjsjJ,@bitxhcrazyh,@mtvvotosCp_112,@pookkyy127,@VeranoMTV2016sg,@lmfor653,@lmfor770,@lmfor786,@lmfor788,@lmfor818,@lmfor785,@lmfor792,@itskawdashians,@lmfor466,@mtvvotosCp_004,@lmfor479,@lmfor488,@5sosaf50,@TropaTTT6,@PaullaBieberSz,@lmfor552,@lmfor572,@lmfor585,@lmfor725,@lmfor470,@lmfor651,@lmfor654,@fifthgold,@nurbanubiebs,@pookkyy132,@lmfor409,@saradggg,@lmfor758,@lmfor752,@lmfor764
	Mentions: @globalfivesauce,@YasminSantosx,@JBPROJETO24HRS,@zaynandcourtney,@ProjetoTagsJB,@magconfhelp,@LukeTheRebel,@inworldcoldplay,@parxchutes,@ValeIbanezJH,@bxringrxy,@worthsantana,@HalaliZJM,@IronicBadBeyxo,@Jennycat1R,@ProjetooBieber,@JBChart,@naxkkskxmx

	Hashtag:  #KCAMexico (23 tweets)
	Authors:  @jorjaodobraz,@angie_odamercy,@MaraIriarte2,@DulceteSantiago,@KarolQuerozene,@julionalexandro,@InfoDEUA,@folks_dmes,@DMApoyoPeru,@Mutanoverdi,@CaroDmLove,@Aly_AbeMateo,@DmChile1,@fernadinhawj,@DulceteDMaria,@DulceJuntos,@gabizeradm,@DulceMariaDS,@mylovearigb,@KellenyeKathyus,@alejand10847424,@DulceSantiagoMa,@DannaZ1996
	Mentions: @AMDaiArgentina,@dudaebelinha261,@DebMarquezE,@jancarlobg

	Hashtag:  #DulceMaria (17 tweets)
	Authors:  @jorjaodobraz,@DulceteSantiago,@KarolQuerozene,@julionalexandro,@InfoDEUA,@DmChile1,@fernadinhawj,@DulceteDMaria,@DulceJuntos,@gabizeradm,@DulceMariaDS,@folks_dmes,@DMApoyoPeru,@Mutanoverdi,@CaroDmLove,@DulceSantiagoMa,@KellenyeKathyus

	Hashtag:  #DulceMariaTrendy (17 tweets)
	Authors:  @jorjaodobraz,@DulceteSantiago,@KarolQuerozene,@julionalexandro,@InfoDEUA,@DmChile1,@fernadinhawj,@DulceteDMaria,@DulceJuntos,@gabizeradm,@DulceMariaDS,@folks_dmes,@DMApoyoPeru,@Mutanoverdi,@CaroDmLove,@DulceSantiagoMa,@KellenyeKathyus

	Hashtag:  #SelfieForTheDolanTwins (16 tweets)
	Authors:  @vale_852,@idolizedolans,@julisacaylen,@LittleGrier_,@dolanftknj,@futuredolan,@Nevedouglas5,@stelinadolan,@holdmedirksen_,@wertuvey,@CashewDolans,@only4tuesdays,@larrybossz,@WILDDOLANS,@DerianONeill,@kail6600
	Mentions: @EthanDolan,@GraysonDolan,@hippiedudejc,@LittleGrier_,@ANABELAMRTINS

	Hashtag:  #VeranoMTV2016 (16 tweets)
	Authors:  @teamahfod26,@Tah_camz,@CAMRENPROIBIDAO,@mtvvotosCp_266,@RenataMFH5,@hootisa_,@teamahfod42,@teamahfod61,@mtvvotosCp_162,@teamahfod62,@teamahfod57,@harmonizer1996x,@Ca_Kordei,@mtvvotosCp_286,@mtvvotosCp_112,@mtvvotosCp_004
	Mentions: @natalliesilvap,@Projeto5HVotes,@5hmonamour,@5HlovatosDay,@Lauraa_KL

	Hashtag:  #OneDirectionIsOverParty (10 tweets)
	Authors:  @LouiShamelesss,@HaroKitzia,@bruna_conde,@maddiempz2878,@ftniallerandbae,@nava_1d49,@Kinda_Weird_Tho,@theirishchonce,@YaresiRangel,@Lodo_bella_
	Mentions: @h4rrycouldfly,@WorldDMs1,@nandos_mars,@thestylesfandom,@callywilk,@NarryMyDream,@M4RVEL0UIS,@teawafer

	Hashtag:  #矢澤にこ生誕祭2016 (10 tweets)
	Authors:  @spring_koharu,@Kannagi_line,@tosyotai,@kamui_0830_,@prince_John7,@31BvhCPHH9qdvX6,@nayuta_forever9,@nyanko943,@PShunji,@Lright1081
	Mentions: @seira_lalala_,@onepiece11114,@comess0,@Tomoya637995711,@Natsuki03824185,@Q7KfJCNH2MzfMus,@ganba_ruby,@tokui_sorangley,@mayudo_lv25,@shino_kotori

	Hashtag:  #ALDUBGetThemLolaTini (8 tweets)
	Authors:  @tragedy_joan,@dulceferido,@RihnaRamos,@OxChoppy,@julie_pacia,@AracanCorazon,@chloebeatriz72,@ariston_joie
	Mentions: @alialsz_aq2,@kimmalberto,@tragedy_joan,@neiltann,@AisAuh,@legion_ligaya8,@MAINEnatics_OFC,@mainedcm,@arzlgarcia,@AlDubBigBoyz,@aldenrichards02,@noriegee,@SenyoraRihanna,@ImTidora

	Hashtag:  #TeenWolfSDCC (6 tweets)
	Authors:  @jetblacklarrie,@pamelacrystinaa,@chickenofbea,@amandacdamasio,@heavysoal,@iodicesvoice
	Mentions: @MTVteenwolf,@NewsTeenWolfBR,@TEEN_WOLFFR_,@voidstilinskj

	Hashtag:  #TeenChoice (6 tweets)
	Authors:  @RizeyMonster,@LaMoglieDiLuke,@abby0512,@rawan931,@SakuraKuchiki,@AngiieGalviis23
	Mentions: @tedoydurosehun,@GraysonDolan,@EthanDolan,@itsruthb,@ShadyFacts5H,@FifthHarmony,@REFAN_SJ,@cata1615,@MileyCyrus

	Hashtag:  #التعليم_المصري_محتاج (6 tweets)
	Authors:  @AttyaEbrahim2,@ziena333,@mnonaaz,@Dedy00000,@120k0007,@melzanaty1111
	Mentions: @martinasamer68,@alaakhaled953,@eraky_ibrahim,@Amr_EL_MaZaGnGi,@sosa99soo,@AlganMoha,@ahmedd_saad

	Hashtag:  #ودي_اقولك (6 tweets)
	Authors:  @crgz52,@MtemplinTemplin,@xiwvqi1,@Joodi_Almutairi,@LauraLtello,@PerlmanGay
	Mentions: @MoniiiiiiRose,@viio_5

	Hashtag:  #24YearsKillingWithKindness (5 tweets)
	Authors:  @_waiting4you_,@mendesxcupcake,@stonelouiscold,@jelenaschild_,@TomyC8
	Mentions: @agbscutiepie,@malikdefenses,@sameoldays,@heyselenabrasil

	Hashtag:  #RTした人全員フォローする (5 tweets)
	Authors:  @kamui_0830_,@prince_John7,@31BvhCPHH9qdvX6,@nayuta_forever9,@Lright1081
	Mentions: @Tomoya637995711,@Natsuki03824185,@Q7KfJCNH2MzfMus,@ganba_ruby,@shino_kotori

	Hashtag:  #on (4 tweets)
	Authors:  @7ndwice,@ioisoumi123,@doyieonf,@SiyeonPG

	Hashtag:  #SDCC2016 (4 tweets)
	Authors:  @xxDistrustedxx,@StocktonAmanda,@HedaSilverWolfe,@L_yOdakim
	Mentions: @DavidMLatt,@TheLastShipTNT,@BrenFosterReal,@IGN,@DebnamCarey,@FearTWD,@RichardSHarmon,@Delta

	Hashtag:  #RNCinCLE (4 tweets)
	Authors:  @CriswellsOk,@WakeupHudsonOH,@CowgirlUp035,@shellz360
	Mentions: @asamjulian,@RichardAngwin,@TEN_GOP,@CNN

	Hashtag:  #NowPlaying (4 tweets)
	Authors:  @TeamFMTracks,@GundackerRocks,@lexi_loves5sos,@mert_ozsevinc
	Mentions: @WNDYRadio365,@SONORDRUMN,@gundacker1,@GundackerRocks,@WinTBM,@Arzaylea

	Hashtag:  #QueroIdelfonsoNoBBB17 (4 tweets)
	Authors:  @MuitoSDV,@PoderSDV,@ReciteiDoTumblr,@SomandoSDV
	Mentions: @joatez12,@boninho,@IdelfonsoReal,@bbb,@Jardel_BrazLL



