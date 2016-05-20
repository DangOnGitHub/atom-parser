package com.dangdoan.atomparser.utils;

import android.util.Xml;

import com.dangdoan.atomparser.models.Feed;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dangdoan on 5/11/16.
 */
public class FeedParser {
    private static final String ns = null;
    private static final int TAG_ID = 1;
    private static final int TAG_TITLE = 2;
    private static final int TAG_SUMMARY = 3;
    private static final int TAG_LINK = 4;
    private static final int TAG_PUBLISHED = 5;

    public List<Feed> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }

    private List<Feed> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<Feed> entries = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("entry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Feed readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String id = "";
        String title = "";
        String summary = "";
        String link = "";
        long published = 0;
        DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("id")) {
                id = readTag(parser, TAG_ID);
            } else if (name.equals("title")) {
                title = readTag(parser, TAG_TITLE);
            } else if (name.equals("summary")) {
                summary = readTag(parser, TAG_SUMMARY);
            } else if (name.equals("link")) {
                link = readTag(parser, TAG_LINK);
            } else if (name.equals("published")) {
                published = formatter.parseMillis(readTag(parser, TAG_PUBLISHED));
            } else {
                skip(parser);
            }
        }
        return new Feed(id, title, summary, link, published);
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readTag(XmlPullParser parser, int tagType) throws IOException, XmlPullParserException {
        switch (tagType) {
            case TAG_ID:
                return readBasicTag(parser, "id");
            case TAG_TITLE:
                return readBasicTag(parser, "title");
            case TAG_SUMMARY:
                return readBasicTag(parser, "summary");
            case TAG_LINK:
                return readLink(parser);
            case TAG_PUBLISHED:
                return readBasicTag(parser, "published");
            default:
                throw new IllegalArgumentException("Unknown tag type: " + tagType);
        }
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")) {
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    private String readBasicTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return result;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
