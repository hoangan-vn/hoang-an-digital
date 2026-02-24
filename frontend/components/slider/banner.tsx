import { Button } from '@/components/ui/button';
import Link from 'next/link';
import Image from 'next/image';

export default function Banner() {
  return (
    <div className='relative flex h-[50vh] w-full items-center justify-center overflow-hidden'>
      <div className='relative h-full w-full'>
        <Image
          src='/image.png'
          alt={`Wedding moment`}
          layout='fill'
          objectFit='cover'
          className=''
        />
      </div>

      {/* Overlay for Dimming and Text Readability */}
      <div className='bg-opacity-60 absolute inset-0 flex flex-col items-center justify-center bg-black p-4 text-center'>
        {/* Main Content */}
        <h1 className='animate-slide-up mb-4 text-4xl font-bold tracking-wide text-white drop-shadow-lg sm:text-5xl md:text-6xl'>
          CINELOVE
        </h1>
        <h2 className='animate-slide-up animation-delay-200 mb-6 text-3xl font-semibold text-white drop-shadow-lg sm:text-4xl md:text-5xl'>
          Make your love story
        </h2>
        <p className='animate-fade-in animation-delay-400 mb-8 max-w-2xl text-base leading-relaxed text-gray-300 sm:text-lg md:text-xl'>
          Hãy để Cinelove giúp bạn kể câu chuyện tình yêu qua từng tập phim –
          <br />
          mang đầy cảm xúc như một thước phim lãng mạn
        </p>

        {/* Call to Action Button */}
        <Link href='/get-started' passHref>
          <Button className='animate-fade-in animation-delay-600 transform rounded-full bg-pink-500 px-8 py-6 text-lg font-semibold text-white shadow-lg transition-all duration-300 ease-in-out hover:scale-105 hover:bg-pink-600 sm:text-xl'>
            Bắt đầu <span className='ml-2 animate-pulse'>→</span>
          </Button>
        </Link>
      </div>

      {/* Tailwind CSS keyframes for animations */}
      <style jsx global>{`
        @keyframes fadeIn {
          from {
            opacity: 0;
          }
          to {
            opacity: 1;
          }
        }
        @keyframes slideUp {
          from {
            opacity: 0;
            transform: translateY(20px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
        .animate-fade-in {
          animation: fadeIn 1s ease-out forwards;
        }
        .animate-slide-up {
          animation: slideUp 0.8s ease-out forwards;
        }
        .animation-delay-200 {
          animation-delay: 0.2s;
        }
        .animation-delay-400 {
          animation-delay: 0.4s;
        }
        .animation-delay-600 {
          animation-delay: 0.6s;
        }
      `}</style>
    </div>
  );
}
