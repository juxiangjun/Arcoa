/*
 * 台灣郵遞區號 3+2 碼 jQuery Plugin 1.0
 * Website: http://app.essoduke.org/twzipcode32/
 *
 * Copyright(c) 2010 essoduke.org
 *
 * This content is released under the MIT License:
 * http://opensource.usrbinruby.net/osi3.0/licenses/mit-license.html (English)
 * http://lucien.cc/?p=15 (Chinese Tradition)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 『AS IS』, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
(function($) {
    
    $.fn.extend({

        twzipcode32: function(options) {
        
            var o = jQuery.extend({
                dataPath: './data/',
                countyName: '',
                areaName: '',
                roadName: '',
                zipName: '',
                countySel: '',
                areaSel: '',
                roadSel: '',
                zipSel: '',
                zipReadonly: true,
                css: []
            }, options);

            var twzipcode_result = [];
            var twzipcode_road_result = [];
            var _ = $(this);
            var sel = {};
            var zipcode = $.fn.twzipcode32.zipcode;
            var opt = ['<option value="">縣市</option>', '<option value="">鄉鎮市區</option>', '<option value="">路名</option>'];
            var ie = !$.support.noCloneEvent;

            try {

                /*
                 * build the elements for county, area and road list
                 */
                //判斷若有對應的element則不建立form object Modified By Maggie 2011.07.25
                sel.zip  = ( o.zipName ) ? ( $('input[name=' + o.zipName + ']').length > 0 ? $('input[name=' + o.zipName + ']') : _.append('<input type="text" name="' + o.zipName + '[]" />').children('input:eq(0)') ) : _.append('<input type="text" name="zip_code[]" />').children('input:eq(0)');
                //sel.zip = _.append('<input type="text" name="' + (0 === o.zipName.length ? 'zip_code' : o.zipName ) + '" />').children('input:eq(0)') ;
                sel.county = ( o.countyName ) ? ( $('select[name=' + o.countyName + ']').length > 0 ? $('select[name=' + o.countyName + ']') : _.append('<select name="' + o.countyName + '[]" />').children('select:eq(0)') ) : _.append('<select name="zip_county[]" />').children('select:eq(0)');
                //sel.county = _.append('<select name="' + (0 === o.countyName.length ? 'zip_county' : o.countyName ) + '" />').children('select:eq(0)') ;
                sel.area = ( o.areaName ) ? ( $('select[name=' + o.areaName + ']').length > 0 ? $('select[name=' + o.areaName + ']') : _.append('<select name="' + o.areaName + '[]" />').children('select:eq(1)') ) : _.append('<select name="zip_area[]" />').children('select:eq(1)');
                //sel.area = _.append('<select name="' + (0 === o.areaName.length ? 'zip_area' : o.areaName ) + '" />').children('select:eq(1)') ;
                sel.road = ( o.roadName) ? ( $('select[name=' + o.roadName+ ']').length > 0 ? $('select[name=' + o.roadName+ ']') : _.append('<select name="' + (0 === o.roadName.length ? 'zip_road' : o.roadName ) + '" />').children('select:eq(2)') ) : _.append('<select name="zip_road[]" />').children('select:eq(2)');
                //sel.road = _.append('<select name="' + (0 === o.roadName.length ? 'zip_road' : o.roadName ) + '" />').children('select:eq(2)') ;
               
                /*
                 * ajax Start and Stop event
                 */
                $('body')
                  .ajaxStart(function() {
                      sel.road.attr('disabled', true);
                  })
                  .ajaxStop(function() {
                      sel.road.removeAttr('disabled');
                });

            }
            catch(e){}

            /*
             * customize css
             */
            sel.county.addClass(o.css && 'undefined' === typeof(o.css[0]) ? '' : o.css[0]);
            sel.area.addClass(o.css && 'undefined' === typeof(o.css[1]) ? '' : o.css[1]);
            sel.road.addClass(o.css && 'undefined' === typeof(o.css[2]) ? '' : o.css[2]);
            sel.zip.addClass(o.css && 'undefined' === typeof(o.css[3]) ? '' : o.css[3]).attr('readonly', o.zipReadonly);
            sel.county.empty().append(opt[0]);
            
            var i = 0, tpl = [];
            
            for (var data in zipcode) {
                if (data) {
                    tpl[i++] = '<option value="';
                    tpl[i++] = data;
                    tpl[i++] = '">';
                    tpl[i++] = data;
                    tpl[i++] = '</option>';
                }
            }

            /*
             * county list build and bind the change event
             */
            sel.county.append(tpl.join('')).val(o.countySel).attr('selected', true).change(function() {
                var i = 0;
                var tpl = [];
                if (0 === sel.county.val().length) {
                    sel.area.empty().append(opt[1]).trigger('change');
                    sel.road.empty().append(opt[2]).trigger('change');
                    sel.zip.val('');
                }
                else {
                    for (var data in zipcode[sel.county.val()]) {
                        if ('CODE' !== data) {
                            tpl[i++] = '<option value="';
                            tpl[i++] = data;
                            tpl[i++] = '">';
                            tpl[i++] = data;
                            tpl[i++] = '</option>';
                        }
                    }
                    if (ie) {
                        var ie6 = $(sel.area)[0];
                        ie6.options.length = parseInt((i/5), 10);
                    }                   
                    sel.road.empty().append(opt[2]).trigger('change');
                    sel.area.empty().append(opt[1]+tpl.join('')).val(o.areaSel).attr('selected', true).trigger('change'); 
                }
            });

            /*
             * compare the zip and append matched to the array
             */
            var comparezip = function(sel, data) {
                var result = [];
                if ('object' === typeof(data)) {
                  for (var i in data){
                      if (sel === data[i].zipcode || sel === data[i].zipcode.substr(0, 3)) {
                          result = [data[i].road, data[i].zipcode, data[i].scope];
                          twzipcode_road_result.push(result);
                      }
                  }
                }
            };

            /*
             * enter the zipcode to find the county, area and road data
             */
            var fromzip = function(val) {

                var result = [];
                var area = '';
                var prefixCounty = val.length >= 3 ? val.substr(0, 3) : '';
                var prefixRoad = val.length === 5 ? val.substr(0, 5) : '';
                var roadmap = [];
                var tpl = [];
                var y = 0;

                for (var i in zipcode) {
                    if (i) {
                        for (var j in zipcode[i]) {
                            if (prefixCounty == zipcode[i][j]) {

                                result.push(i);
                                result.push(j);
                                area = j;
                                
                                //if (5 === val.length) {
                                    $.getJSON(o.dataPath + zipcode[i].CODE + '.js', function(json) {
                                    
                                        twzipcode_road_result = [];
                                        comparezip(val, json[0][area]);

                                        if (0 < twzipcode_road_result.length) {

                                            y = 0;
                                            tpl = [];

                                            for (var r in twzipcode_road_result) {
                                                if (r) {
                                                    tpl[y++] = '<option value="';
                                                    tpl[y++] = twzipcode_road_result[r][0];
                                                    tpl[y++] = '" class="';
                                                    tpl[y++] = twzipcode_road_result[r][1];
                                                    tpl[y++] = '">';
                                                    tpl[y++] = twzipcode_road_result[r][0] + ' (' + twzipcode_road_result[r][2] + ')';
                                                    tpl[y++] = '</option>';
                                                }
                                            }

                                            if (ie) {
                                                var ie6 = $(sel.road)[0];
                                                ie6.options.length = parseInt((y/5), 10);
                                            }
					    sel.road.empty().append(opt[2]+tpl.join('')).children("."+o.zipSel+"[value='"+o.roadSel+"']").attr('selected', true).trigger('change');
                            		    //sel.road.empty().append(tpl.join('')).val(o.roadSel).attr('selected', true).trigger('change');
                                        }
                                        
                                    });
                                    break;
                                //}
                            }
                        }
                    }
                }

                return result;
            };

            /*
             * seach the math data and append to array
             */
            var search = function(z, obj) {
                var rg;
                var zip = [];
                for (var i in obj) {
                    if ('undefined' !== i) {

                        if ('object' === typeof(obj[i])) {
                            search(z, obj[i]);
                        }

                        if ('zipcode' === i) {
                            rg = new RegExp('^' + z + '+');
                            if (-1 !== obj[i].toString().search(rg)) {
                                zip = [obj.road, obj.zipcode, obj.scope];
                                twzipcode_result.push(zip);
                            }
                        }
                    }
                }
            };

            /*
             * area list change event
             */
            var areachanged = function() {

                var area = $(this).val();
		
                if (0 !== sel.county.val().length && 0 !== sel.area.val().length) {

                    if (zipcode[sel.county.val()][area]) {

                        var zip = zipcode[sel.county.val()][area];
                        var code = zipcode[sel.county.val()].CODE;

                        sel.road.empty().append(opt[2]);

                        $.getJSON(o.dataPath + code + '.js', function(json) {

                            twzipcode_result = [];
                            search(zip, json[0][area]);

                            var i = 0;
                            var tpl = [];
                            for (var r in twzipcode_result) {
                                if (r) {
                                    tpl[i++] = '<option value="';
                                    tpl[i++] = twzipcode_result[r][0];
                                    tpl[i++] = '" class="';
                                    tpl[i++] = twzipcode_result[r][1];
                                    tpl[i++] = '">';
                                    tpl[i++] = twzipcode_result[r][0] + ' (' + twzipcode_result[r][2] + ')';
                                    tpl[i++] = '</option>';
                                }
                            }

                            if (ie) {
                                var ie6 = $(sel.road)[0];
                                ie6.options.length = parseInt((i/5), 10);
                            }

                            sel.road.empty().append(opt[2]+tpl.join('')).children("."+o.zipSel+"[value='"+o.roadSel+"']").attr('selected', true).trigger('change');
                            //sel.road.empty().append(tpl.join('')).val(o.roadSel).attr('selected', true).trigger('change');
                        });
                    }
                }else{
                    sel.road.empty().append(opt[2]).trigger('change');
                    sel.zip.val('');
                }
            };

            /*
             * road list change event
             */
            var roadchanged = function() {
                var zip = $(this).children('option:selected').attr('class');
                sel.zip.val(zip);
                twzipcode_result = [];
                twzipcode_road_result = [];
            };

            // bind the change event for area list
            sel.area.bind('change', areachanged);

            // bind the change event fot road list
            sel.road.bind('change', roadchanged);

            // select county and trigger the change event
            sel.county.val(o.countySel).attr('selected', true).trigger('change');

            // bind the keyup event for zip input
            sel.zip
            .focus(function() {
                sel.area.unbind('change');
                sel.road.unbind('change');
            })
            .keyup(function(e) {

                if (13 === e.which || (48 > e.which && 57 < e.which)) {
                    return false;
                }

                $(this).val($(this).val().replace(/[A-Za-z\s]/g, ''));

                var val = $(this).val();

                if (0 === val.length) {
                    return;
                }

                var _countyAndarea = fromzip(val);

                if (2 === _countyAndarea.length) {

                    sel.county.val(_countyAndarea[0]).attr('selected', true).trigger('change');
                    sel.area.val(_countyAndarea[1]).attr('selected', true).trigger('change');
                    
                    if (5 === val.length) {
                        sel.area.bind('change', areachanged);
                        sel.road.bind('change', roadchanged);
                    }
                    else {
                        sel.area.unbind('change');
                        sel.road.unbind('change');
                    }

                    return;
                }

            });

            $('input[type=reset]').click(function() {sel.area.empty().append(opt[1]);});

          }

    });
    
    // 3 code data
    $.fn.twzipcode32.zipcode = {
      '基隆市': {'仁愛區':'200', '信義區':'201', '中正區':'202', '中山區':'203', '安樂區':'204', '暖暖區':'205', '七堵區':'206', 'CODE':'C'},
      '台北市': {'中正區':'100', '大同區':'103', '中山區':'104', '松山區':'105', '大安區':'106', '萬華區':'108', '信義區':'110', '士林區':'111', '北投區':'112', '內湖區':'114', '南港區':'115', '文山區':'116', 'CODE':'A'},
      '新北市': {
      '萬里區':'207', '金山區':'208', '板橋區':'220', '汐止區':'221', '深坑區':'222', '石碇區':'223',
      '瑞芳區':'224', '平溪區':'226', '雙溪區':'227', '貢寮區':'228', '新店區':'231', '坪林區':'232',
      '烏來區':'233', '永和區':'234', '中和區':'235', '土城區':'236', '三峽區':'237', '樹林區':'238',
      '鶯歌區':'239', '三重區':'241', '新莊區':'242', '泰山區':'243', '林口區':'244', '蘆洲區':'247',
      '五股區':'248', '八里區':'249', '淡水區':'251', '三芝區':'252', '石門區':'253', 'CODE':'F'
      },
      '宜蘭縣': {
        '宜蘭市':'260', '頭城鎮':'261', '礁溪鄉':'262', '壯圍鄉':'263', '員山鄉':'264', '羅東鎮':'265',
        '三星鄉':'266', '大同鄉':'267', '五結鄉':'268', '冬山鄉':'269', '蘇澳鎮':'270', '南澳鄉':'272',
        '釣魚台列嶼':'290', 'CODE':'G'
      },
      '新竹市': {'北區':'300','東區':'300','香山區':'300', 'CODE':'O'},
      '新竹縣': {
        '竹北市':'302', '湖口鄉':'303', '新豐鄉':'304', '新埔鎮':'305', '關西鎮':'306', '芎林鄉':'307',
        '寶山鄉':'308', '竹東鎮':'310', '五峰鄉':'311', '橫山鄉':'312', '尖石鄉':'313', '北埔鄉':'314',
        '峨眉鄉':'315', 'CODE':'J'
      },
      '桃園縣': {
        '中壢市':'320', '平鎮市':'324', '龍潭鄉':'325', '楊梅市':'326', '新屋鄉':'327', '觀音鄉':'328',
        '桃園市':'330', '龜山鄉':'333', '八德市':'334', '大溪鎮':'335', '復興鄉':'336', '大園鄉':'337',
        '蘆竹鄉':'338', 'CODE':'H'
      },
      '苗栗縣': {
        '竹南鎮':'350', '頭份鎮':'351', '三灣鄉':'352', '南庄鄉':'353', '獅潭鄉':'354', '後龍鎮':'356',
        '通霄鎮':'357', '苑裡鎮':'358', '苗栗市':'360', '造橋鄉':'361', '頭屋鄉':'362', '公館鄉':'363',
        '大湖鄉':'364', '泰安鄉':'365', '銅鑼鄉':'366', '三義鄉':'367', '西湖鄉':'368', '卓蘭鎮':'369', 'CODE':'K'
      },
      '台中市': {
	  '中區':'400', '東區':'401', '南區':'402', '西區':'403', '北區':'404', '北屯區':'406', '西屯區':'407', '南屯區':'408',
      '太平區':'411', '大里區':'412', '霧峰區':'413', '烏日區':'414', '豐原區':'420', '后里區':'421',
      '石岡區':'422', '東勢區':'423', '和平區':'424', '新社區':'426', '潭子區':'427', '大雅區':'428',
      '神岡區':'429', '大肚區':'432', '沙鹿區':'433', '龍井區':'434', '梧棲區':'435', '清水區':'436',
      '大甲區':'437', '外埔區':'438', '大安區':'439', 'CODE':'L'
      },
      '彰化縣': {
        '彰化市':'500', '芬園鄉':'502', '花壇鄉':'503', '秀水鄉':'504', '鹿港鎮':'505', '福興鄉':'506',
        '線西鄉':'507', '和美鎮':'508', '伸港鄉':'509', '員林鎮':'510', '社頭鄉':'511', '永靖鄉':'512',
        '埔心鄉':'513', '溪湖鎮':'514', '大村鄉':'515', '埔鹽鄉':'516', '田中鎮':'520', '北斗鎮':'521',
        '田尾鄉':'522', '埤頭鄉':'523', '溪州鄉':'524', '竹塘鄉':'525', '二林鎮':'526', '大城鄉':'527',
        '芳苑鄉':'528', '二水鄉':'530', 'CODE':'N'
      },
      '南投縣': {
        '南投市':'540', '中寮鄉':'541', '草屯鎮':'542', '國姓鄉':'544', '埔里鎮':'545', '仁愛鄉':'546',
        '名間鄉':'551', '集集鎮':'552', '水里鄉':'553', '魚池鄉':'555', '信義鄉':'556', '竹山鎮':'557',
        '鹿谷鄉':'558', 'CODE':'M'
      },
      '嘉義市': {'東區':'600','西區':'600', 'CODE':'I'},
      '嘉義縣': {
        '番路鄉':'602', '梅山鄉':'603', '竹崎鄉':'604', '阿里山鄉':'605', '中埔鄉':'606', '大埔鄉':'607',
        '水上鄉':'608', '鹿草鄉':'611', '太保市':'612', '朴子市':'613', '東石鄉':'614', '六腳鄉':'615',
        '新港鄉':'616', '民雄鄉':'621', '大林鎮':'622', '溪口鄉':'623', '義竹鄉':'624', '布袋鎮':'625', 'CODE':'Q'
      },
      '雲林縣': {
        '斗南鎮':'630', '大埤鄉':'631', '虎尾鎮':'632', '土庫鎮':'633', '褒忠鄉':'634', '東勢鄉':'635',
        '台西鄉':'636', '崙背鄉':'637', '麥寮鄉':'638', '斗六市':'640', '林內鄉':'643', '古坑鄉':'646',
        '莿桐鄉':'647', '西螺鎮':'648', '二崙鄉':'649', '北港鎮':'651', '水林鄉':'652', '口湖鄉':'653',
        '四湖鄉':'654', '元長鄉':'655', 'CODE':'P'
      },
      '台南市': {
	  '中西區':'700', '東區':'701', '南區':'702', '北區':'704', '安平區':'708', '安南區':'709',
      '永康區':'710', '歸仁區':'711', '新化區':'712', '左鎮區':'713', '玉井區':'714', '楠西區':'715',
      '南化區':'716', '仁德區':'717', '關廟區':'718', '龍崎區':'719', '官田區':'720', '麻豆區':'721',
      '佳里區':'722', '西港區':'723', '七股區':'724', '將軍區':'725', '學甲區':'726', '北門區':'727',
      '新營區':'730', '後壁區':'731', '白河區':'732', '東山區':'733', '六甲區':'734', '下營區':'735',
      '柳營區':'736', '鹽水區':'737', '善化區':'741', '大內區':'742', '山上區':'743', '新市區':'744',
      '安定區':'745', 'CODE':'R'
      },
      '高雄市': {
      '新興區':'800', '前金區':'801', '苓雅區':'802', '鹽埕區':'803', '鼓山區':'804', '旗津區':'805',
      '前鎮區':'806', '三民區':'807', '楠梓區':'811', '小港區':'812', '左營區':'813',
      '仁武區':'814', '大社區':'815', '岡山區':'820', '路竹區':'821', '阿蓮區':'822', '田寮區':'823',
      '燕巢區':'824', '橋頭區':'825', '梓官區':'826', '彌陀區':'827', '永安區':'828', '湖內區':'829',
      '鳳山區':'830', '大寮區':'831', '林園區':'832', '鳥松區':'833', '大樹區':'840', '旗山區':'842',
      '美濃區':'843', '六龜區':'844', '內門區':'845', '杉林區':'846', '甲仙區':'847', '桃源區':'848',
      '那瑪夏區':'849', '茂林區':'851', '茄萣區':'852', 'CODE':'S'
      },
      '屏東縣': {
        '屏東市':'900', '三地門鄉':'901', '霧台鄉':'902', '瑪家鄉':'903', '九如鄉':'904', '里港鄉':'905',
        '高樹鄉':'906', '鹽埔鄉':'907', '長治鄉':'908', '麟洛鄉':'909', '竹田鄉':'911', '內埔鄉':'912',
        '萬丹鄉':'913', '潮州鎮':'920', '泰武鄉':'921', '來義鄉':'922', '萬巒鄉':'923', '崁頂鄉':'924',
        '新埤鄉':'925', '南州鄉':'926', '林邊鄉':'927', '東港鎮':'928', '琉球鄉':'929', '佳冬鄉':'931',
        '新園鄉':'932', '枋寮鄉':'940', '枋山鄉':'941', '春日鄉':'942', '獅子鄉':'943', '車城鄉':'944',
        '牡丹鄉':'945', '恆春鎮':'946', '滿州鄉':'947', 'CODE':'T'
      },
      '台東縣': {
        '台東市':'950', '綠島鄉':'951', '蘭嶼鄉':'952', '延平鄉':'953', '卑南鄉':'954', '鹿野鄉':'955',
        '關山鎮':'956', '海端鄉':'957', '池上鄉':'958', '東河鄉':'959', '成功鎮':'961', '長濱鄉':'962',
        '太麻里鄉':'963', '金峰鄉':'964', '大武鄉':'965', '達仁鄉':'966', 'CODE':'V'
      },
      '花蓮縣': {
        '花蓮市':'970', '新城鄉':'971', '秀林鄉':'972', '吉安鄉':'973', '壽豐鄉':'974', '鳳林鎮':'975',
        '光復鄉':'976', '豐濱鄉':'977', '瑞穗鄉':'978', '萬榮鄉':'979', '玉里鎮':'981', '卓溪鄉':'982',
        '富里鄉':'983', 'CODE':'U'
      },
      '金門縣': {'金沙鎮':'890', '金湖鎮':'891', '金寧鄉':'892', '金城鎮':'893', '烈嶼鄉':'894', '烏坵鄉':'896', 'CODE':'W'},
      '連江縣': {'南竿鄉':'209', '北竿鄉':'210', '莒光鄉':'211', '東引鄉':'212', 'CODE':'Z'},
      '澎湖縣': {'馬公市':'880', '西嶼鄉':'881', '望安鄉':'882', '七美鄉':'883', '白沙鄉':'884', '湖西鄉':'885', 'CODE':'X'},
      '南海諸島': {'東沙':'817', '南沙':'819'}
    };
})(jQuery);