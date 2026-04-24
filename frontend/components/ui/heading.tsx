import Title from 'antd/es/typography/Title'
import { Typography } from 'antd'

interface HeadingProps {
  title: string
  description: string
}

export const Heading: React.FC<HeadingProps> = ({ title, description }) => {
  return (
    <Typography>
      <Title level={2} className="my-0! font-bold! tracking-tight!">
        {title}
      </Title>
      <Title level={5} className="mt-2! font-normal! text-muted-foreground!">
        {description}
      </Title>
    </Typography>
  )
}
