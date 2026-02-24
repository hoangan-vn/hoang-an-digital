import { cn } from "@/lib/utils";

type SectionHeadingProps = {
  title: string;
  description?: string;
  rightSlot?: React.ReactNode;
  className?: string;
};

export function SectionHeading({
  title,
  description,
  rightSlot,
  className,
}: SectionHeadingProps) {
  return (
    <div
      className={cn(
        "flex flex-col gap-2 md:flex-row md:items-end md:justify-between",
        className
      )}
    >
      <div>
        <h2 className="text-balance text-2xl font-semibold tracking-tight text-white md:text-3xl">
          {title}
        </h2>
        {description ? (
          <p className="mt-1 max-w-2xl text-pretty text-sm text-white/70 md:text-base">
            {description}
          </p>
        ) : null}
      </div>
      {rightSlot ? <div className="pt-1 md:pt-0">{rightSlot}</div> : null}
    </div>
  );
}

