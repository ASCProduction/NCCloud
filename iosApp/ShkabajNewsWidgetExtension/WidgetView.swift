//
//  WidgetView.swift
//  ShkabajNewsWidgetExtension
//
//  Created by Andrey Karaulov on 07.10.2020.
//  Copyright © 2020 Shkabaj. All rights reserved.
//

import WidgetKit
import SwiftUI

struct HeaderView: View {
    
    var body: some View {
        HStack {
            Text("Shkabaj - Lajme")
                .font(.headline)
                .foregroundColor(Color(hex: "#00ABEF"))
            Spacer()
            Image("shkabaj_icon")
        }
    }
    
}

struct NewsRow: View {
    
    var entry: NewsEntry
    
    var body: some View {
        GeometryReader { geometry in
            HStack {
                Text(entry.title)
                    .font(.subheadline)
                    .frame(maxHeight: .infinity)
                    .lineLimit(2)
                if let img = entry.uiImage {
                    Spacer()
                    Image(uiImage: img)
                        .resizable()
                        .scaledToFill()
                        .frame(maxWidth: 60.0, maxHeight: geometry.size.height)
                        .clipped()
                        .cornerRadius(8)
                }
            }
        }
    }
    
}

struct ShkabajNewsWidgetView: View {
    
    @Environment(\.colorScheme) var colorScheme
    
    var newsListEntry: TopNewsListEntry

    var body: some View {
        VStack(spacing: 5) {
            HeaderView().padding(.bottom, 10)
            if newsListEntry.news.isEmpty {
                Text("Load news error!")
                    .font(.largeTitle)
                    .frame(maxHeight: .infinity)
            } else {
                ForEach(0..<newsListEntry.news.count, id: \.self) { i in
                    NewsRow(entry: newsListEntry.news[i])
                }
            }
        }
        .padding()
        .widgetBackground(colorScheme == .dark ? Color.black : Color.white)
        .widgetURL(Constants.widgetURL)
    }
}

struct ShkabajNewsWidgetView_Previews: PreviewProvider {
    static var previews: some View {
        ShkabajNewsWidgetView(newsListEntry: TopNewsListEntry())
            .previewContext(WidgetPreviewContext(family: .systemLarge))
    }
}

extension View {
    
    func widgetBackground(_ backgroundView: some View) -> some View {
        if #available(iOSApplicationExtension 17.0, *) {
            return containerBackground(for: .widget) {
                backgroundView
            }
        } else {
            return background(backgroundView)
        }
    }
    
}
