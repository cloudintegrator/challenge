FROM node:18-alpine


RUN addgroup -g 10014 choreo && \
    adduser  --disabled-password  --no-create-home --uid 10014 --ingroup choreo choreouser


WORKDIR /app


ENV HOST="0.0.0.0"
ENV DISABLE_DEV_SERVER_HOST_CHECK=true
ENV HTTPS=false

COPY . .
USER 10014

RUN chown 10014 .
RUN npm install
EXPOSE 3000
CMD ["npm", "start"]