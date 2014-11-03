package com.yamap55.url2ch.service

import org.jsoup.*
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/**
 * 2chにアクセスを行うクラス。
 */
public class Access2chService {

    private static final String BBS_TABLE_URL = 'http://menu.2ch.net/bbstable.html'

    /**
     * 2chの板一覧を取得する。
     * @return List[0] : 板名, List[1] : URL
     */
    public def getBbsList() {

        // 取得一覧から除く板名
        List ignoreBBSList = [
                '2ch総合案内', '地震ヘッドライン', '地震速報', '臨時地震', '臨時地震+', '緊急自然災害@超臨時',
                'テレビ番組欄',
                '2chプロジェクト',
                'いろいろランキング',
                '削除依頼', '批判要望',
        ]

        Document doc = Jsoup.connect(BBS_TABLE_URL).get()
        Elements anchors = doc.getElementsByTag('a')

        List bbsList = []
        String reg = /^http:\/\/\w+\.2ch\.net\/\w+\/$/

        anchors.each { Elements elem ->
            String name = elem.text()
            String url = elem.attr('href')
            if ((url ==~ reg) && (!ignoreBBSList.contains(name))) {
                bbsList << [name, url]
            }
        }
    }

    /**
     * 指定された板のスレッド一覧を取得する。
     * @param bbsName 板名
     * @param bbsUrl 板URL
     * @return List[0] : 板名, List[1] : URL
     */
    def getThreadList(bbsName, bbsUrl) {
        // 板URLのリストの処理
        List threads = []
        println("取得中: " + bbsName)

        String subjects = new URL(bbsUrl + 'subject.txt').getText('SHIFT_JIS')

        try {
            // スレ一覧を取得
            // スレ一覧のループ
            subjects.eachLine { String line ->
                // 該当するメソッド名を抽出
                (line =~ /^(\d+)\.dat<>(.+)\(\d+\)$/).each { String all, String threadId, String threadName ->
                    threads << [bbsName: bbsName, bbsUrl: bbsUrl, threadName: threadName, threadId: threadId]
                }
            }
        } catch (e) {
            println("!!! ERROR !!! " + [bbsName, bbsUrl, e.message])
        }
        return threads
    }
}
