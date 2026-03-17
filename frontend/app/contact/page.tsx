import Link from "next/link";
import { cookies } from "next/headers";

import { SectionHeading } from "@/components/site/section-heading";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { getCompany, type Lang } from "@/lib/server/data";

async function getLang(): Promise<Lang> {
  const value = (await cookies()).get("lang")?.value;
  return value === "en" ? "en" : "vi";
}

export default async function LienHePage() {
  const lang = await getLang();
  const company = await getCompany();

  const social = company.social ?? {};
  const mail = social.email?.value ?? "";
  const facebook = social.facebook?.value ?? "";
  const zalo = social.zalo?.value ?? "";
  const instagram = social.instagram?.value ?? "";
  const tiktok = social.tiktok?.value ?? "";
  const youtube = social.youtube?.value ?? "";

  return (
    <div className="space-y-8 pb-14">
      <SectionHeading
        title={lang === "vi" ? "Liên hệ" : "Contact"}
        description={
          lang === "vi"
            ? "Kết nối với CineLove qua mạng xã hội hoặc email."
            : "Reach out via social channels or email."
        }
      />

      <div className="grid gap-4 md:grid-cols-2">
        <Card className="border-white/10 bg-white/5 text-white">
          <CardHeader>
            <CardTitle className="text-base">
              {lang === "vi" ? "Kênh hỗ trợ" : "Support channels"}
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-2 text-sm text-white/70">
            {mail ? (
              <div>
                Email:{" "}
                <a className="text-pink-300 hover:text-pink-200" href={`mailto:${mail}`}>
                  {mail}
                </a>
              </div>
            ) : null}
            {zalo ? (
              <div>
                Zalo:{" "}
                <Link className="text-pink-300 hover:text-pink-200" href={zalo} target="_blank" rel="noopener noreferrer">
                  {zalo}
                </Link>
              </div>
            ) : null}
            {facebook ? (
              <div>
                Facebook:{" "}
                <Link className="text-pink-300 hover:text-pink-200" href={facebook} target="_blank" rel="noopener noreferrer">
                  {facebook}
                </Link>
              </div>
            ) : null}
          </CardContent>
        </Card>

        <Card className="border-white/10 bg-white/5 text-white">
          <CardHeader>
            <CardTitle className="text-base">
              {lang === "vi" ? "Mạng xã hội" : "Social"}
            </CardTitle>
          </CardHeader>
          <CardContent className="flex flex-wrap gap-2 text-sm">
            {[
              ["Instagram", instagram],
              ["TikTok", tiktok],
              ["YouTube", youtube],
            ]
              .filter(([, url]) => Boolean(url))
              .map(([label, url]) => (
                <Link
                  key={label}
                  href={url as string}
                  className="rounded-full border border-white/15 bg-white/5 px-4 py-2 text-white/80 hover:bg-white/10 hover:text-white"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  {label}
                </Link>
              ))}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

