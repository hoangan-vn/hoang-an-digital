"use client";

import Link from "next/link";

import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/components/ui/carousel";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export type TemplateCarouselItem = {
  id: string;
  title: string;
  description: string;
  priceLabel: string;
  href?: string;
};

export function TemplateCarousel({
  title,
  items,
}: {
  title?: string;
  items: TemplateCarouselItem[];
}) {
  return (
    <div className="space-y-4">
      {title ? <div className="text-lg font-semibold text-white">{title}</div> : null}

      <div className="relative">
        <Carousel
          opts={{
            align: "start",
            dragFree: true,
          }}
          className="px-10"
        >
          <CarouselContent className="-ml-3">
            {items.map((item) => (
              <CarouselItem
                key={item.id}
                className="basis-[80%] pl-3 sm:basis-1/2 lg:basis-1/4"
              >
                <Card className="border-white/10 bg-white/5 text-white">
                  <CardHeader className="space-y-1">
                    <CardTitle className="line-clamp-2 text-base">
                      {item.title}
                    </CardTitle>
                    <div className="text-sm text-white/60">{item.priceLabel}</div>
                  </CardHeader>
                  <CardContent className="text-sm text-white/70">
                    <p className="line-clamp-3">{item.description}</p>
                    {item.href ? (
                      <div className="mt-3">
                        <Link
                          href={item.href}
                          className="text-sm font-medium text-pink-300 hover:text-pink-200"
                        >
                          Xem chi tiết →
                        </Link>
                      </div>
                    ) : null}
                  </CardContent>
                </Card>
              </CarouselItem>
            ))}
          </CarouselContent>
          <CarouselPrevious className="-left-2 border-white/15 bg-white/5 text-white hover:bg-white/10" />
          <CarouselNext className="-right-2 border-white/15 bg-white/5 text-white hover:bg-white/10" />
        </Carousel>
      </div>
    </div>
  );
}

