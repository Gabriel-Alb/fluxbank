export function AuthVisualPanel() {
  return (
    <div className="relative h-full min-h-[460px] w-full overflow-hidden rounded-2xl bg-[#df5a2d]">
      <div
        aria-hidden
        className="auth-background-field pointer-events-none absolute -inset-[20%]"
      />

      <div
        aria-hidden
        className="auth-center-glow pointer-events-none absolute left-[18%] top-[17%] h-[70%] w-[72%] rounded-[45%] bg-[#d85a32]/45 blur-[85px]"
      />

      <div
        aria-hidden
        className="auth-left-light pointer-events-none absolute -left-[30%] top-[27%] h-[62%] w-[72%] rounded-full bg-[#ffe4a0]/95 blur-[90px]"
      />

      <div
        aria-hidden
        className="auth-bottom-left-light pointer-events-none absolute -left-[8%] bottom-[-10%] h-[48%] w-[62%] rounded-full bg-[#ffc45f]/80 blur-[90px]"
      />

      <div
        aria-hidden
        className="auth-top-right-light pointer-events-none absolute -right-[25%] top-[-8%] h-[55%] w-[65%] rounded-full bg-[#f8783e]/80 blur-[90px]"
      />

      <div
        aria-hidden
        className="auth-bottom-right-light pointer-events-none absolute -right-[25%] bottom-[-12%] h-[62%] w-[68%] rounded-full bg-[#f79a43]/85 blur-[105px]"
      />

      <div
        aria-hidden
        className="auth-bottom-light pointer-events-none absolute -bottom-[25%] left-[5%] h-[48%] w-[92%] rounded-[50%] bg-[#ffb44f]/70 blur-[105px]"
      />

      <div
        aria-hidden
        className="pointer-events-none absolute inset-0 opacity-[0.13] mix-blend-soft-light"
        style={{
          backgroundImage:
            'url("data:image/svg+xml,%3Csvg viewBox=%270 0 180 180%27 xmlns=%27http://www.w3.org/2000/svg%27%3E%3Cfilter id=%27noise%27%3E%3CfeTurbulence type=%27fractalNoise%27 baseFrequency=%270.9%27 numOctaves=%274%27 stitchTiles=%27stitch%27/%3E%3C/filter%3E%3Crect width=%27100%25%27 height=%27100%25%27 filter=%27url(%23noise)%27 opacity=%270.7%27/%3E%3C/svg%3E")',
        }}
      />

      <div className="relative z-10 flex h-full flex-col px-8 py-8 lg:px-10 lg:py-10">
        <div>
          <span className="text-[17px] font-semibold tracking-[-0.025em] text-[#271b1a]">
            FluxBank
          </span>
        </div>

        <div className="mt-auto max-w-[450px] pb-1">
          <p className="text-sm font-medium leading-6 text-[#3b211e]/70">
            Controle financeiro de forma simples.
          </p>

          <h2 className="mt-1.5 text-[clamp(1.9rem,2.8vw,2.7rem)] font-semibold leading-[1.12] tracking-[-0.04em] text-[#281917]">
            Tenha sua vida financeira organizada em um só lugar.
          </h2>
        </div>
      </div>

      <style>
        {`
          .auth-background-field {
            background:
              radial-gradient(
                ellipse 58% 54% at 55% 47%,
                rgba(190, 72, 39, 0.72) 0%,
                rgba(207, 82, 38, 0.62) 34%,
                rgba(226, 99, 40, 0.42) 64%,
                transparent 82%
              ),
              radial-gradient(
                circle at 5% 58%,
                rgba(255, 226, 150, 0.95) 0%,
                rgba(255, 188, 83, 0.50) 32%,
                transparent 58%
              ),
              radial-gradient(
                circle at 96% 82%,
                rgba(249, 151, 64, 0.92) 0%,
                transparent 55%
              ),
              linear-gradient(
                180deg,
                #d14b2b 0%,
                #e66332 38%,
                #ef823b 68%,
                #f5a34a 100%
              );

            background-size: 125% 125%;
            animation: auth-background-drift 16s ease-in-out infinite alternate;
            will-change: transform, background-position;
          }

          .auth-center-glow {
            animation: auth-center-move 14s ease-in-out infinite alternate;
            will-change: transform;
          }

          .auth-left-light {
            animation: auth-left-move 18s ease-in-out infinite alternate;
            will-change: transform;
          }

          .auth-bottom-left-light {
            animation: auth-bottom-left-move 20s ease-in-out infinite alternate;
            will-change: transform;
          }

          .auth-top-right-light {
            animation: auth-top-right-move 17s ease-in-out infinite alternate;
            will-change: transform;
          }

          .auth-bottom-right-light {
            animation: auth-bottom-right-move 21s ease-in-out infinite alternate;
            will-change: transform;
          }

          .auth-bottom-light {
            animation: auth-bottom-move 19s ease-in-out infinite alternate;
            will-change: transform;
          }

          @keyframes auth-background-drift {
            0% {
              background-position: 0% 0%;
              transform: scale(1);
            }

            50% {
              background-position: 55% 45%;
              transform: scale(1.04) rotate(0.6deg);
            }

            100% {
              background-position: 100% 85%;
              transform: scale(1.08) rotate(-0.5deg);
            }
          }

          @keyframes auth-center-move {
            0% {
              transform: translate3d(-8%, -4%, 0) scale(0.95);
            }

            100% {
              transform: translate3d(12%, 9%, 0) scale(1.12);
            }
          }

          @keyframes auth-left-move {
            0% {
              transform: translate3d(-10%, 7%, 0) scale(1);
            }

            100% {
              transform: translate3d(18%, -10%, 0) scale(1.14);
            }
          }

          @keyframes auth-bottom-left-move {
            0% {
              transform: translate3d(-5%, 10%, 0) scale(0.95);
            }

            100% {
              transform: translate3d(15%, -15%, 0) scale(1.15);
            }
          }

          @keyframes auth-top-right-move {
            0% {
              transform: translate3d(12%, -8%, 0) scale(1);
            }

            100% {
              transform: translate3d(-18%, 14%, 0) scale(1.16);
            }
          }

          @keyframes auth-bottom-right-move {
            0% {
              transform: translate3d(10%, 10%, 0) scale(0.95);
            }

            100% {
              transform: translate3d(-16%, -14%, 0) scale(1.12);
            }
          }

          @keyframes auth-bottom-move {
            0% {
              transform: translate3d(-5%, 12%, 0) scale(1);
            }

            100% {
              transform: translate3d(10%, -16%, 0) scale(1.12);
            }
          }

          @media (prefers-reduced-motion: reduce) {
            .auth-background-field,
            .auth-center-glow,
            .auth-left-light,
            .auth-bottom-left-light,
            .auth-top-right-light,
            .auth-bottom-right-light,
            .auth-bottom-light {
              animation: none;
            }
          }
        `}
      </style>
    </div>
  )
}