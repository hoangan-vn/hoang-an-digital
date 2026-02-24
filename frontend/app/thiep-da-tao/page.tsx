import Link from "next/link";
import { cookies } from "next/headers";

import { SectionHeading } from "@/components/site/section-heading";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { getCardCustomer, pickLocalized, type Lang } from "@/lib/server/data";

async function getLang(): Promise<Lang> {
  const value = (await cookies()).get("lang")?.value;
  return value === "en" ? "en" : "vi";
}

export default async function ThiepDaTaoPage() {
  const lang = await getLang();
  const cardCustomer = await getCardCustomer();
  const cards = cardCustomer.cards;

  return (
    <div className="space-y-8 pb-14">
      <SectionHeading
        title={lang === "vi" ? "Thiệp đã tạo" : "Created invitations"}
        description={
          lang === "vi"
            ? "Quản lý các card khách mời theo nhóm, theo dõi RSVP nhanh."
            : "Manage guest cards by groups and track RSVPs."
        }
      />

      <div className="grid gap-4 md:grid-cols-2">
        {cards.map((c) => (
          <Card key={c.id} className="border-white/10 bg-white/5 text-white">
            <CardHeader className="space-y-1">
              <CardTitle className="text-base">
                {pickLocalized(c.title, lang)}
              </CardTitle>
              <div className="text-sm text-white/60">
                {pickLocalized(c.event.name, lang)} • {c.event.date} {c.event.time}
              </div>
            </CardHeader>
            <CardContent className="space-y-3 text-sm text-white/70">
              <div className="line-clamp-2">
                {pickLocalized(c.event.locationName, lang)} — {c.event.address}
              </div>
              <div className="flex flex-wrap gap-2 text-xs text-white/60">
                <span>
                  Guests: <b className="text-white">{c.stats?.guestCount ?? 0}</b>
                </span>
                <span>
                  Attending:{" "}
                  <b className="text-white">{c.stats?.attendingCount ?? 0}</b>
                </span>
                <span>
                  Pending:{" "}
                  <b className="text-white">{c.stats?.pendingCount ?? 0}</b>
                </span>
              </div>
              <div>
                <Link
                  href={c.event.mapUrl}
                  className="text-sm font-medium text-pink-300 hover:text-pink-200"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  {lang === "vi" ? "Xem bản đồ" : "View map"} →
                </Link>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}

