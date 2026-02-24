"use client";

import Link from "next/link";
import { PhoneCall, MessageCircle, Facebook } from "lucide-react";

type StickyContactBarProps = {
  phone?: string;
  zaloUrl?: string;
  facebookUrl?: string;
};

function IconButton({
  href,
  label,
  children,
}: {
  href: string;
  label: string;
  children: React.ReactNode;
}) {
  return (
    <Link
      href={href}
      aria-label={label}
      className="grid size-12 place-items-center rounded-full bg-white/10 text-white shadow-lg backdrop-blur transition hover:bg-white/15 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-pink-400"
      target="_blank"
      rel="noopener noreferrer"
    >
      {children}
    </Link>
  );
}

export function StickyContactBar({
  phone,
  zaloUrl,
  facebookUrl,
}: StickyContactBarProps) {
  const telHref = phone ? `tel:${phone.replace(/\s+/g, "")}` : "";
  return (
    <div className="fixed bottom-6 right-6 z-50 flex flex-col gap-3">
      {facebookUrl ? (
        <IconButton href={facebookUrl} label="Facebook">
          <Facebook className="size-5" />
        </IconButton>
      ) : null}
      {zaloUrl ? (
        <IconButton href={zaloUrl} label="Zalo">
          <MessageCircle className="size-5" />
        </IconButton>
      ) : null}
      {phone ? (
        <IconButton href={telHref} label="Call">
          <PhoneCall className="size-5" />
        </IconButton>
      ) : null}
    </div>
  );
}

